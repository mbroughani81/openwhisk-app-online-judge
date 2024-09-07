# openwhisk-app-online-judge

This project is a simple online judge system written in **Clojure**, designed for deployment on the **OpenWhisk** platform. The application uses **Postgresql database** for persisting the data.

## Local Setup

To experiment with this project locally, you can set up OpenWhisk on your system by following the instructions here:
[OpenWhisk Standalone Setup](https://github.com/apache/openwhisk/tree/master/core/standalone)

### Prerequisites
- **PostgreSQL**: Ensure you have a PostgreSQL database set up locally or on a server.
- **Clojure and Leiningen**: Used for building the project.
- **OpenWhisk CLI (wsk)**: For creating and managing actions in OpenWhisk.

---

## Usage


### Create Uberjar

To create an **uberjar** for the project, run the following command:

\`\`\`bash
lein uberjar
\`\`\`


### Deploy Actions to OpenWhisk

To create the actions in OpenWhisk, execute the provided shell script:

\`\`\`bash
./wsk-commands-up.sh
\`\`\`

### Configuration

Actions require specific configurations, which are passed as parameters when creating the actions. An example configuration can be found in the \`resources/configs/db.json\` file.

**TODO**: Extend the configuration to include additional parameters as needed.

---

## Defined Actions

### 1. Creating a Problem

Each problem created in the system includes:
- **Time limits**: Maximum time allowed for a solution to execute.
- **Memory limits**: Maximum memory usage allowed.
- **Input/output pairs**: A list showing expected inputs and outputs for a correct submission.

Example of invoking this action can be found in the script:
\`create-problem-sample/invoke-create-problem.sh\`

**TODO**: Implement logic to enforce time and memory limits for submitted solutions (currently ignored).

### 2. Creating a Submission

Each submission consists of the following:
- **Problem ID**: The ID of the problem being solved.
- **Code**: The submitted code.
- **Language**: The programming language of the submitted code.

Example of invoking this action can be found in the script:
\`create-submit-sample/invoke-create-submit.sh\`

### 3. Evaluating a Submission

This action evaluates a given submission based on its **ID**. The system will execute the submission and update its status in the database (e.g., passed, failed, etc.).

Example of invoking this action can be found in the script:
\`eval-submit-sample/invoke-eval-submit.sh\`

### 4. Evaluating Code

This action evaluates a piece of code against a list of inputs and generates corresponding outputs. It is used internally for evaluating submissions.

Example of invoking this action can be found in the script:
\`run-code-sample/invoke-eval-code.sh\`

**TODO**: Currently, only **JavaScript** code is supported. Add support for additional programming languages as needed.

---

## License

Copyright © 2024 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
