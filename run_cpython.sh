./gradlew :cpythonadapter:CPythonBuild
export LD_LIBRARY_PATH=./build/cpython_build/lib
export PYTHONHOME=./build/cpython_build
./build/cpython_build/bin/python3 -s
