/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user manual at https://docs.gradle.org/5.4.1/userguide/multi_project_builds.html
 */

rootProject.name = "zillions"

include("zillions-api")
include("zillions-junit")
include("zillions-env")
include("zillions-core")
include("zillions-testbed")
include("zillions-biginteger")
include("zillions-gmp")

// Trick copied from https://github.com/robfletcher/strikt/blob/master/settings.gradle.kts
// Explained at http://www.developerphil.com/renaming-your-gradle-build-files
rootProject.children.forEach {
  it.buildFileName = "${it.name}.gradle.kts"
}
