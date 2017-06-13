set "PATH=%PATH%;C:\Program Files\Java\jdk1.8.0_111\bin"

javac ../Android/BrausteuerungApp/app/src/main/java/de/mikrosikaru/brausteuerungapp/Constants.java -g -d ./
javac ../Android/BrausteuerungApp/app/src/main/java/de/mikrosikaru/brausteuerungapp/SystemBrewStep.java -g -d ./
javac ../Android/BrausteuerungApp/app/src/main/java/de/mikrosikaru/brausteuerungapp/SystemSettings.java -g -d ./
javac ../Android/BrausteuerungApp/app/src/main/java/de/mikrosikaru/brausteuerungapp/BrewRecipeFile.java -g -d ./
javac ../Android/BrausteuerungApp/app/src/main/java/de/mikrosikaru/brausteuerungapp/BrewRecipeFileXsud.java -g -d ./
javac BrewRecipeFileTester.java

java BrewRecipeFileTester Hallo

Pause
cmd .