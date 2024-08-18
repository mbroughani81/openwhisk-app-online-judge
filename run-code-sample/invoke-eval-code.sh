# Read the code from code.js
CODE=$(cat ./code.js)

# Read the input from input.json
INPUT=$(cat ./input.json)

# Create the command and invoke the action
PAYLOAD=$(printf '{"code": "%s", "param": %s}' "$CODE" "$INPUT")
echo "$PAYLOAD" | wsk action invoke clj-eval-code --param-file /dev/stdin --result



