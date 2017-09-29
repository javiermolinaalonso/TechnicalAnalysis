#!/usr/bin/env bash

java -classpath jooq-3.9.6.jar;jooq-meta-3.9.6.jar;jooq-codegen-3.9.6.jar;mysql-connector-java-5.1.18-bin.jar;. org.jooq.util.GenerationTool library.xml