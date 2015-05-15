README for tradebrowser
==========================

EXPORT data
mlcp.sh export -host localhost -port 8004 -username admin -password admin -mode local -output_file_path exported -compress true

IMPORT data

mlcp.sh import -host localhost -port 8004 -username admin -password admin -input_file_path exported -mode local