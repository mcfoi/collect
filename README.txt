IRRI GeODK Collect depends on JAVAROSA lib

Javarosa is a simple Java project that must be built using Ant.
This can be easily done in Eclipse so the whole Javarosa project is delivered as an Eclipse project.
The build can be started from the build.xml file in \javarosa\core dir, using the "package" task.
This will produce a .jar file in \javarosa\core\dist\ dir whit the name 
javarosa-libraries-2014-04-29-custom4lai.jar
The name comes from teh fact that, despite the original m.sundt\javarose project on Bitucket
is years ahead, the version of GeoODK Collect targeted at time of this document writing is
version 1.4.5, and GeoODK Collect 1.4.5 depends on Javarosa 2014-04-29.
So the Javarosa project was forked in mcfoi Bitbucket profile, the cloned and reverted to 
revision 3205 (the one tagged with string "javarosa-2014-04-29") with the mercurial commands:

hg update --rev 3205
  Then a new brancj was created wt
hg checkout 
	providing the name of the new branch "javarosa-2014-04-29-irri"
	and finally committing all changes to start the new branch
hg committ

In this new branch, a set of customization were introduced to support the new DATATYPE_LAI

Once this was done, the file
\javarosa\core\build.xml
was changed to skip Unit Tests, skip Javadoc creation and output a file named
"javarosa-libraries-2014-04-29-custom4lai.jar" (instead of javarosa-libraries.jar")
At this step the project was built using the Exlipe contextual menu on 
and selcting "Run as.. -> Ant buld"

Teh output file was copied in the Android Studio project, inside folder
RRI GeoODK collect\app\libs to take the place of the original
file "javarosa-libraries-2014-04-29.jar"
(that was renamed "javarosa-libraries-2014-04-29.j__" for safety)