# Eclipse and Gradle

**Importing Skyve as a Gradle project**

1. Clone a new skyve repo (git@github.com:skyvers/skyve.git)
1. Install the gradle buildship plugin for eclipse
1. Select _File->Import_
1. Select _Existing Gradle Project_ and click _Next_
1. Skip the welcome screen
1. Select the project root and click _Next_
1. Ensure that the _Gradle wrapper_ is being used as the gradle distribution and click _Next_
1. All projects should be listed under project structure
1. Click _Finish_
1. The eclipse .classpath and .project files will be modified, don't commit these modifications as 
they should eventually be removed from version control

**Notes**

- Tasks can be run via the Gradle Tasks tab
- Tasks can be run via the command line using: _gradlew [taskName]_
- _gradlew build -x test_ executes a build and skips tests
- _gradlew install_ installs jars to the local maven cache