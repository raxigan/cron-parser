#!/usr/bin/env kscript

@file:DependsOnMaven("com.homework:cron-parser:0.0.1-SNAPSHOT")

com.homework.CronExpressionParser().parse(args)
