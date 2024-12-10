FROM ubuntu:latest
LABEL authors="thiri"

ENTRYPOINT ["top", "-b"]