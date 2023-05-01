#!/bin/bash
docker build -t gemini_interview . && docker run -i --entrypoint /app/execute_test.sh --rm gemini_interview