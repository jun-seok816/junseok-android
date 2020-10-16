# MainActivity클래스

# RequirePerms()메소드

## Description 

 - sms 권한을 얻어옵니다.

## Parameter

- 없음
    
## Return 

 - type : void
 
 - value : 없음

## Dependence function

- requestPermissions() 
  - 권한 요청 코드를 직접 관리할 수 있게하는 메소드. 
  - 호출에 파라미터로 요청 코드를 포함합니다
  - https://developer.android.com/training/permissions/requesting?hl=ko

## Source code 
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

# sendSms()메소드

## Description 

 - 문자 기반 sms를 보냅니다.

## Parameter

- String PhoneNumber

- String message
    
## Return 

 - type : void
 
 - value : 없음

## Dependence function

- getDefalut() 
  - 기본 구독 ID와 연결된 SmsManager를 가져옵니다.
  - https://developer.android.com/reference/java/util/Locale
  
- sendTextMessage()
  - 문자 기반 SMS를 보냅니다

## Source code 
```
 private void sendSMS(String phoneNumber,String message){
        SmsManager smsManager= SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber,null,message,null,null);

    }
```

# onNewIntent()메소드

## Description 

 - 실행한 Activity가 포그라운드인 상태에서 Intent에 값을 추가하고 StartActivity를 호출할때 onnewIntent 메소드가 호출된다.

## Parameter

- 없음
    
## Return 

 - type : void
 
 - value : 없음

## Dependence function

- ShowSMS() 

## Source code 

```
 @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ShowSMS(intent);
    }
```

# ShowSMS메소드

## Description 

 - 받아온 메세지 값을 토스트 메세지로 띄움

## Parameter

- Intent intent
    
## Return 

 - type : void
 
 - value : 없음

## Dependence function

- getStringExtra()
  - 인텐트에서 확장 데이터를 검색합니다.

## Source code 

```
public void ShowSMS(Intent intent) {

        if (intent != null) {
            String  sender = intent.getStringExtra("sender");
            String content = intent.getStringExtra("content");


            tv_sender = sender;
            tv_content = content;

            Toast.makeText(getApplicationContext(),tv_content,Toast.LENGTH_SHORT).show()

        }

    }
```


# MyReceiver클래스

# Manifest


## Description 

 - MyReceiver서브 클래스에 대하여 인텐트 필터를 추가하여 어느 유형의 인텐트를 수락하는지 지정합니다.

## Source code 

```
 <receiver
            android:name=".MyReceiver"
            android:enabled="true"
            android:exported="true">

            <intent-filter>
                <action android:name="android.provider.Telephony.SMS_RECEIVED"/>
            </intent-filter>
        </receiver>
```

# OnReceive 메소드

## Description 

 - 이 메서드는 BroadcastReceiver가 Intent 브로드 캐스트를 수신 할 때 호출됩니다.
   - https://developer.android.com/reference/android/content/BroadcastReceiver

## Parameter

- Context context
- Intent intent
    
## Return 

 - type : void
 
 - value : 없음

## Dependence function

- sendToActivity()

- getExtras()
  - getExtras()를 이용해서 데이터를 받을 수 있다.
  - 인텐트에서 확장 된 데이터의 맵을 검색합니다.
  - https://developer.android.com/reference/android/content/Intent

- getOriginatingAddress()
  - 이 메소드는 실제 송신자 번호를 알려줍니다
  
- getMessageBody()
  - 메시지 본문이 존재하고 텍스트기반인 경우 문자열로 반환합니다.
  
- getTimestampMillis()
  - currentTimeMillis () 형식으로 서비스 센터 타임 스탬프를 반환합니다.
  - https://developer.android.com/reference/android/telephony/SmsMessage

## Source code 
```
@Override
    public void onReceive(Context context, Intent intent) {


        Log.d(TAG,"onReceive() called");

        Bundle bundle= intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);

        if (messages.length>0){
            String sender= messages[0].getOriginatingAddress();
            String content= messages[0].getMessageBody().toString();
            

            Log.d(TAG,"sender"+sender);
            Log.d(TAG, "content:"+content);


            sendToActivity(context,sender,content);
        }

    }
```

# SendToActivity()메소드

## Description 

 - 얻어온 sender content 변수를 intent를 사용하여 MainActivity로 값을 넘김

## Parameter

- Context context
- String sender
- Date date
    
## Return 

 - type : void
 
 - value : 없음

## Dependence function

- addFlags()
  - 인텐트에 추가 플래그를 추가합니다.
  - https://medium.com/@logishudson0218/intent-flag%EC%97%90-%EB%8C%80%ED%95%9C-%EC%9D%B4%ED%95%B4-d8c91ddd3bfc
  
- putExtra()
  - 액티비티 이동과 동시에 이전 액티비티에서 이동하는 액티비티로 어떤 값을 넘기고 싶을때 쓰는 함수
  - https://dpdpwl.tistory.com/22
  
- startActivity()
  -  Intent로 지정된 DisplayMessageActivity의 인스턴스를 시작합니다. 
  - https://developer.android.com/training/basics/firstapp/starting-activity?hl=ko
## Source code 
```
private void sendToActivity(Context context, String sender, String content, Date date){
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                |Intent.FLAG_ACTIVITY_SINGLE_TOP
                |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("sender", sender);
        intent.putExtra("content", content);
        intent.putExtra("date", format.format(date));
        context.startActivity(intent);
    }
```
# parseSmsMessage()

## Description 

 - SMS문자의 내용을 뽑아내는 정형화된 코드이다.

## Parameter

- 없음
    
## Return 

 - type : SmsMessage[]
 
 - value : messages

## Dependence function

- createFromPdu() 
  - 지정된 메시지 형식을 사용하여 원시 PDU에서 SmsMessage를 만듭니다. 
  - https://developer.android.com/reference/android/telephony/SmsMessage#createFromPdu(byte[],%20java.lang.String)

## Source code 

```
private SmsMessage[] parseSmsMessage(Bundle bundle){
        Object[] objs = (Object[])bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];
        for (int i= 0; i<objs.length; i++){
            messages[i] = SmsMessage.createFromPdu((byte[])objs[i]);
        }return  messages;
    }
```
