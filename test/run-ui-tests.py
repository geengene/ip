#!/usr/bin/env python3
"""Runs UI transcript tests from test/ui-test-plan.md."""

from __future__ import annotations

import re
import subprocess
import sys
from dataclasses import dataclass
from pathlib import Path


PROJECT_ROOT = Path(__file__).resolve().parents[1]
PLAN_PATH = PROJECT_ROOT / "test" / "ui-test-plan.md"
BUILD_DIR = Path("/private/tmp/codex-ip-ui-tests")
SESSION_LOG_PATH = PROJECT_ROOT / "test" / "ui-test-session.log"
DATA_FILE_PATH = PROJECT_ROOT / "data" / "duke.txt"


@dataclass
class TestCase:
    name: str
    aim: str
    setup_data_file: str | None
    inputs: str
    expected_output: str
    expected_data_file: str | None


def parse_test_plan() -> list[TestCase]:
    plan_text = PLAN_PATH.read_text()
    sections = re.split(r"^## Test Case: ", plan_text, flags=re.MULTILINE)[1:]
    test_cases: list[TestCase] = []

    for section in sections:
        name, body = section.split("\n", 1)
        aim_match = re.search(r"^Aim: (.+)$", body, flags=re.MULTILINE)
        inputs_match = re.search(
            r"^### Inputs\n```text\n(.*?)\n```", body, flags=re.MULTILINE | re.DOTALL
        )
        expected_match = re.search(
            r"^### Expected Output\n```text\n(.*?)\n```", body, flags=re.MULTILINE | re.DOTALL
        )
        setup_data_match = re.search(
            r"^### Setup Data File\n```text\n(.*?)\n```", body, flags=re.MULTILINE | re.DOTALL
        )
        expected_data_match = re.search(
            r"^### Expected Data File\n```text\n(.*?)\n```", body, flags=re.MULTILINE | re.DOTALL
        )

        if not aim_match or not inputs_match or not expected_match:
            raise ValueError(f"Test case '{name.strip()}' is missing aim, inputs, or expected output.")

        test_cases.append(
            TestCase(
                name=name.strip(),
                aim=aim_match.group(1).strip(),
                setup_data_file=setup_data_match.group(1) if setup_data_match else None,
                inputs=inputs_match.group(1),
                expected_output=expected_match.group(1),
                expected_data_file=expected_data_match.group(1) if expected_data_match else None,
            )
        )

    return test_cases


def compile_program() -> None:
    source_files = sorted(
        str(path)
        for path in (PROJECT_ROOT / "src" / "main" / "java").rglob("*.java")
        if path.name not in {"Launcher.java", "Main.java", "MainWindow.java"}
    )
    subprocess.run(["javac", "-d", str(BUILD_DIR), *source_files], check=True, cwd=PROJECT_ROOT)


def reset_data_file() -> None:
    if DATA_FILE_PATH.exists():
        DATA_FILE_PATH.unlink()
    try:
        DATA_FILE_PATH.parent.rmdir()
    except FileNotFoundError:
        pass
    except OSError:
        pass


def write_setup_data_file(test_case: TestCase) -> None:
    if test_case.setup_data_file is None:
        return

    DATA_FILE_PATH.parent.mkdir(parents=True, exist_ok=True)
    DATA_FILE_PATH.write_text(test_case.setup_data_file + "\n")


def read_data_file() -> str:
    if not DATA_FILE_PATH.exists():
        return ""
    return DATA_FILE_PATH.read_text().rstrip("\n")


def run_case(test_case: TestCase) -> str:
    result = subprocess.run(
        ["java", "-cp", str(BUILD_DIR), "duke.Duke"],
        input=test_case.inputs + "\n",
        text=True,
        capture_output=True,
        check=False,
        cwd=PROJECT_ROOT,
    )

    if result.returncode != 0:
        raise RuntimeError(
            f"Program exited with code {result.returncode}.\nSTDOUT:\n{result.stdout}\nSTDERR:\n{result.stderr}"
        )

    return result.stdout.rstrip("\n")


def append_transcript(lines: list[str], test_case: TestCase, actual_output: str) -> None:
    lines.extend(
        [
            f"## {test_case.name}",
            f"Aim: {test_case.aim}",
            "",
        ]
    )
    if test_case.setup_data_file is not None:
        lines.extend(
            [
                "Setup data file:",
                test_case.setup_data_file,
                "",
            ]
        )
    lines.extend(
        [
            "Input:",
            test_case.inputs,
            "",
            "Output:",
            actual_output,
            "",
        ]
    )
    if test_case.expected_data_file is not None:
        lines.extend(
            [
                "Data file:",
                read_data_file(),
                "",
            ]
        )


def main() -> int:
    test_cases = parse_test_plan()
    compile_program()

    transcript_lines = ["# UI Test Session", ""]

    for test_case in test_cases:
        reset_data_file()
        write_setup_data_file(test_case)
        actual_output = run_case(test_case)
        append_transcript(transcript_lines, test_case, actual_output)

        if actual_output != test_case.expected_output:
            SESSION_LOG_PATH.write_text("\n".join(transcript_lines))
            print(f"FAILED: {test_case.name}")
            print("\nExpected:")
            print(test_case.expected_output)
            print("\nActual:")
            print(actual_output)
            return 1

        if test_case.expected_data_file is not None:
            actual_data_file = read_data_file()
            if actual_data_file != test_case.expected_data_file:
                SESSION_LOG_PATH.write_text("\n".join(transcript_lines))
                print(f"FAILED: {test_case.name}")
                print("\nExpected data file:")
                print(test_case.expected_data_file)
                print("\nActual data file:")
                print(actual_data_file)
                return 1

    reset_data_file()
    SESSION_LOG_PATH.write_text("\n".join(transcript_lines))
    print(f"Passed {len(test_cases)} UI test case(s).")
    print(f"Session log: {SESSION_LOG_PATH}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
