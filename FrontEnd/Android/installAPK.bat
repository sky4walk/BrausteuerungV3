set ADBDIR=%LOCALAPPDATA%\Android\sdk\platform-tools
#set ADBDIR=%USERPROFILE%\AppData\Local\Android\sdk\platform-tools
%ADBDIR%\adb.exe push BrausteuerungApp\app\app-release.apk /data/local/tmp/de.mikrosikaru.brausteuerungapp
%ADBDIR%\adb.exe shell pm install -r "/data/local/tmp/de.mikrosikaru.brausteuerungapp"
%ADBDIR%\adb.exe shell am start -n "de.mikrosikaru.brausteuerungapp/de.mikrosikaru.brausteuerungapp.MainMenu" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER