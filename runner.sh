#!/bin/bash

################################################################################
##                       Cloud Computing Course                               ##
##                Runner Script for Files v/s Databases                       ##
##                                                                            ##
##            Copyright 2018-2019: Cloud Computing Course                     ##
##                     Carnegie Mellon University                             ##
##   Unauthorized distribution of copyrighted material, including             ##
##  unauthorized peer-to-peer file sharing, may subject the students          ##
##  to the fullest extent of Carnegie Mellon University copyright policies.   ##
################################################################################

################################################################################
##                      README Before You Start                               ##
################################################################################
# Fill in the functions below for each question.
# You may use any programming language(s) in any question.
# You may use other files or scripts in these functions as long as they are in
# the submission folder.
# All files MUST include source code (e.g. do not just submit jar or pyc files).
#
# We will suggest tools or libraries in each question to enrich your learning.
# You are allowed to solve questions without the recommended tools or libraries.
#
# The colon `:` is a POSIX built-in basically equivalent to the `true` command,
# REPLACE it with your own command in each function.
# Before you fill your solution,
# DO NOT remove the colon or the function will break because the bash functions
# may not be empty!


################################################################################
##                          Setup & Cleanup                                   ##
################################################################################

setup() {
  # Fill in this helper function to do any setup if you need to.
  #
  # This function will be executed once at the beginning of the grading process.
  # Other functions might be executed repeatedly in arbitrary order.
  # Make use of this function to reduce unnecessary overhead and to make your
  # code behave consistently.
  #
  # e.g. You should compile Java code in this function.
  #
  # However, DO NOT use this function to solve any question or
  # generate any intermediate output.
  #
  # Examples:
  # javac *.java
  # mvn clean package
  # pip3 install -r requirements.txt
  #
  # Standard output format:
  # No standard output needed
  mvn package -Dmaven.test.skip=true
}

cleanup() {
  # Fill this helper function to clean up if you need to.
  #
  # This function will be executed once at the end of the grading process
  # Other functions might be executed repeatedly in arbitrary order.
  #
  # Examples:
  # mvn clean
  #
  # Standard output format:
  # No standard output needed
  mvn clean
}

################################################################################
##                             Flat Files                                     ##
################################################################################
# In the following sections, you should write a function to compute the answer
# and print it to StdOut for each question.
#
# WARNING:
# Treat every function as an independent and standalone program.
# Any question below should not depend on the result or the intermediate
# output from another.

q1() {
  # Column-wise data processing with awk or pandas
  #
  # Question:
  # How many rows in `yelp_academic_dataset_business.tsv` that contain
  # "Las Vegas" as a substring in the "city" field (case insensitive)?
  #
  # In this question, we recommend 2 different approaches, one with awk and
  # the other with the Pandas library in Python. You are recommended to explore
  # both ways. You are also allowed to choose other approaches.
  #
  # * GNU awk:
  # In GNU awk, fields are separated by whitespaces (spaces, tabs and newlines)
  # by default. Leading and trailing whitespace are ignored.
  # To process a TSV file, you need to set the Field Separator to tabs.
  #
  # You can then use a dollar sign ‘$’ to refer to a field, followed by the
  # number of the field you want. Thus, $1 refers to the first field, $2 to the
  # second, and so on. $0 refers to the whole input record.
  #
  # 1. The "city" field is the 8th column.
  # 2. You can use tolower() or IGNORECASE to search in a case-insensitive way.
  # 3. If the action in a pattern-action statement is absent, the action
  # will be `print $0`.
  #
  # You can solve the question by filling in the template:
  # awk 'BEGIN {set the Field Separator} pattern' input_file | wc -l
  #
  # * pandas:
  # A DataFrame can be thought of as a dictionary of "Series"s: i.e., each
  # column is a Series. The two easiest ways to refer to a particular Series
  # of a DataFrame is to use
  # pandas.DataFrame.col_name or pandas.DataFrame['col_name'], e.g.
  # df.city or df['city']
  #
  # Your task is to solve two problems:
  # 1. There is missing data in the "city" field for some records. The pandas
  # library uses NaN (not a number) to denote the missing data. You need to omit
  # all the NaN values before you use the data as strings to do the substring
  # matching.
  # 2. Find the parameter on case sensitivity in pandas.Series.str.contains.
  #
  # You can solve the question by filling in the template, `__` is the
  # placeholder where you should fill your solution.
  #
  # import pandas as pd
  # s = pd.read_table('yelp_academic_dataset_business.tsv').city.__
  # s[s.str.contains('las vegas', __)].count()
  #
  # Standard output format:
  # <number>
  python q1.py
}

q2() {
  # Database-style group by operation with flat files
  #
  # Question:
  # What is the maximum number of likes that are received by all tips posted by
  # the same user?
  #
  # In this question, we highly recommend the Pandas library in Python. You are
  # recommended to explore other approaches such as sort, sed, and awk. You are
  # also allowed to choose other approaches.
  #
  # * pandas:
  # Pandas can perform SQL’s GROUP BY operations. GROUP BY in pandas is to divide
  # the DataFrame objects into groups by one or more columns, usually for the
  # purpose of applying some function (typically aggregation) to each group.
  #
  # You can solve the question by filling in the template, `__` is the
  # placeholder where you should fill your solution.
  #
  # import pandas as pd
  # s = pd.read_table('yelp_academic_dataset_tip.tsv')
  # s.groupby(__)[__].sum().__
  #
  # Standard output format:
  # <number>
  python q2.py
}

q3() {
  # Database-style join operation with flat files
  #
  # Question:
  # Find the number of users who have posted at least a tip and at least a reivew.
  #
  # In this question, we highly recommend the Pandas library in Python. You are
  # recommended to explore other approaches such as sort, sed, and awk. You are
  # also allowed to choose other approaches.
  #
  # * pandas:
  # Pandas can merge DataFrame objects by performing a database-style join
  # operation by columns or indexes.
  # There're different types of join options in Pandas: ‘left’, ‘right’,
  # ‘outer’, ‘inner’, like in other databases, and the default join is ‘left’.
  # e.g. Join A and B on column key in Pandas will be
  # import pandas as pd
  # pd.merge(A, B, on=key)
  #
  # Hint:
  # Be aware that you're using a micro instance which means resources such as
  # memory is rather limited. Try your best to optimize your QUERY so that you
  # are not running into issues such as Out-Of-Memory (OOM).
  #
  # Standard output format:
  # <number>
  python q3.py
}

################################################################################
##                    DO NOT MODIFY ANYTHING BELOW                            ##
################################################################################
RED_FONT='\033[0;31m'
NO_COLOR='\033[0m'
declare -ar flat_files=( "q1" "q2" "q3")
declare -ar mysql=( "q4" "q5" "q6" "q7" "q8" "q9" "q10")
declare -ar hbase=( "q11" "q12" "q13" "q14")
declare questions=("${flat_files[@]}" "${mysql[@]}" "${hbase[@]}")
last=${questions[$((${#questions[*]}-1))]}
readonly last
readonly usage="This program is used to execute your solution.
Usage:
./runner.sh to run all the questions
./runner.sh -r <question_id> to run one single question
./runner.sh -s to run setup() function
./runner.sh -c to run cleanup() function
Example:
./runner.sh -r q1 to run q1"

contains() {
  local e
  for e in "${@:2}"; do
    [[ "$e" == "$1" ]] && return 0;
  done
  return 1
}

run() {
  if contains "$1" "${flat_files[@]}"; then
    echo -n "$("$1")"
  elif contains "$1" "${mysql[@]}"; then
    echo -n "$(java -cp target/database_tasks.jar edu.cmu.cs.cloud.MySQLTasks "$1")"
  elif contains "$1" "${hbase[@]}"; then
    echo -n "$(java -cp target/database_tasks.jar edu.cmu.cs.cloud.HBaseTasks "$1")"
  fi
}

while getopts ":hr:sc" opt; do
  case $opt in
    h)
      echo "$usage" >&2
      exit
    ;;
    s)
      setup
      echo "setup() function executed" >&2
      exit
    ;;
    c)
      cleanup
      echo "cleanup() function executed" >&2
      exit
    ;;
    r)
      question=$OPTARG
      if contains "$question" "${questions[@]}"; then
        run "$question"
      else
        echo "Invalid question id" >&2
        echo "$usage" >&2
        exit 2
      fi
      exit
    ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      echo "$usage" >&2
      exit 2
    ;;
  esac
done

if [ -z "$1" ]; then
  setup 1>&2
  echo "setup() function executed" >&2
  echo -e "The ${RED_FONT}JSON escaped${NO_COLOR} answers generated by executing your solution are: " >&2
  echo "{"
  for question in "${questions[@]}"; do
    echo -n ' '\""$question"\":"$(run "$question" | python3 -c 'import json, sys; print(json.dumps(sys.stdin.read()))')"
    if [[ "${question}" == "$last" ]]; then
      echo ""
    else
      echo ","
    fi
  done
  echo "}"
  echo ""
  cleanup 1>&2
  echo "cleanup() function executed" >&2
  echo "If you feel these values are correct please run:" >&2
  echo "./submitter" >&2
else
  echo "Invalid usage" >&2
  echo "$usage" >&2
  exit 2
fi
