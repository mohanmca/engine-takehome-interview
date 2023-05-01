#!/bin/bash
docker build -t gemini_interview . && docker run -i --entrypoint /app/execute.sh --rm gemini_interview