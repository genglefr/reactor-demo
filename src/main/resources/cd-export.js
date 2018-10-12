cbexport json -c couchbase://127.0.0.1 -u root -p password -b data -o cb-data.json -f lines
cbimport json -c couchbase://127.0.0.1 -u root -p password -b data -o cb-data.json -f lines