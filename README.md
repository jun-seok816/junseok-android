# junseok-android

SmsBroadcast 어플에 대한 설명
=============

1. 어플에 퍼미션 추가
2. MyReceiver클래스 에 대하여
3. 하이브리드 앱을 이용해 MyReciever클래스로 받은 메세지를 Html 페이지에 띄우기

*어플에 퍼미션 추가
```
<uses-permission android:name="android.permission.RECEIVE_SMS"/>
    <uses-permission android:name="android.permission.SEND_SMS"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.INTERNET"/>
```
안드로이드 스튜디오 Manifest파일에 퍼미션을 추가합니다
퍼미션 추가후
```
 private void requirePerms(){
        String[] permissions={Manifest.permission.RECEIVE_SMS};
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS);
        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,permissions,1);

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);

    }
```
어플이 켜지자마자 requirePerms()메소드를 실행시켜 사용자에게 sms권한을 얻어오게합니다.
