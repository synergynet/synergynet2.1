The build directory is in a state of flux.

What *does* work is builddist.xml[main] which produces useful output
in the dist directory: a MacOSX App and a Windows Executable. It required
SynergyNet.jar to be present in this directory, and this should be built using
the Build Fat Jar plugin.

