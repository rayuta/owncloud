package com.owncloud.android.authentication;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.owncloud.android.R;
import com.owncloud.android.UserData;

public class AccountRegisterDialog extends AlertDialog.Builder  {
    
    
    private UserData mData = UserData.GetInstance();
    private Activity mActivity;
    private Context mContext;
    private EditText et_id,et_password1,et_password2, et_email;
    private String id, pwd, email;
    private Button overlapCheck;
    private TextView overlapresult, pwdmatchresult;

    
    
    
    public AccountRegisterDialog(Context context, Activity activity) {
        super(context);
        this.mActivity = activity;
        this.mContext = context;

        this.setTitle(R.string.create_account);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.account_register_dialog, null);
        this.setView(dialoglayout);

        et_id = (EditText) dialoglayout.findViewById(R.id.edittext_id);

        et_password1 = (EditText) dialoglayout.findViewById(R.id.edittext_pwd);
        et_password2 = (EditText) dialoglayout.findViewById(R.id.edittext_pwd2);
        et_email = (EditText) dialoglayout.findViewById(R.id.edittext_email);
        
        overlapCheck = (Button) dialoglayout.findViewById(R.id.overcheck);
        overlapresult = (TextView) dialoglayout.findViewById(R.id.overlapresult);
        pwdmatchresult = (TextView) dialoglayout.findViewById(R.id.pwdmatchresult);
        
        et_password2.addTextChangedListener(mWatcher);

        
        overlapCheck.setOnClickListener(clicklistener);
  
    }
    
    TextWatcher mWatcher = new TextWatcher() {
        
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            
               if(et_password1.getText().toString().equals(s.toString())){
                   
                   pwdmatchresult.setText("OK");
                   pwdmatchresult.setVisibility(View.VISIBLE);
               }
               else{
                   pwdmatchresult.setText("X");
                   pwdmatchresult.setVisibility(View.VISIBLE);
               }
         
           
        }
       
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                      int after) {
               // TODO Auto-generated method stub
              
        }
       
        @Override
        public void afterTextChanged(Editable s) {
               // TODO Auto-generated method stub
              
        }
  };

    
    OnClickListener clicklistener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(v.getId()==R.id.overcheck){
                
                
                try {
                   String result =  new AccountNetworkHandler(getContext()).execute("3", et_id.getText().toString()).get();
                    if("998".equals(result)){
                        mData.resetData();
                        overlapresult.setText("OK");
                        overlapresult.setVisibility(View.VISIBLE);
                       
                    } else {
                        mData.resetData();
                        overlapresult.setText("NO");
                        overlapresult.setVisibility(View.VISIBLE);
                       
                    }
                    
                    
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                
            }
        }
    };
    
    
    
    public boolean checkInfo(){
        if(("OK".equals(overlapresult.getText().toString())
                &&("OK".equals(pwdmatchresult.getText().toString())))){
            id = et_id.getText().toString();
            pwd = et_password2.getText().toString();
            email = et_email.getText().toString();
            
            return true;
        }
        else return false;
    }
    
    public void sendInfo(){
        new AccountNetworkHandler(getContext()).execute("1", id, pwd, email);     // params[0]= 1����,2����,3��ȸ
        new AccountNetworkHandler(getContext()).execute("3", id);
    }
    
    
    
    
      

    
    public String getId() {
        return id; // ����Ʈ �̸��� ��ȯ�ϴ� �޼ҵ�
    }
    public String getPWD() {
        return pwd; // ����Ʈ �ּҸ� ��ȯ�ϴ� �޼ҵ�
    }
    public String getEmail(){
        return email;
    }
}
    
/*
    @Override // ��ġ ������
    public boolean onTouch(View v, MotionEvent event) {
        // Ȯ�� ��ư�� Ŭ���ϸ� �Է��� ���� ������ ������ �� ���̾�α׸� ����
        if (v == addOK) {
            id = et_id.getText().toString(); // �Էµ� ����Ʈ �̸��� ����
            pwd = et_password.getText().toString(); // �Էµ� ����Ʈ �ּҸ� ����
            email = et_email.getText().toString(); // �Էµ� ����Ʈ �ּҸ� ����
            dismiss(); // ���� MainActivity���� �������� Dismiss �����ʰ� �۵���
        }

        // ��� ��ư�� Ŭ���ϸ� �ܼ��� ���̾�α׸� ����
        else if (v == addCancel)
            dismiss(); // ���� MainActivity���� �������� Dismiss�� Cancel �����ʰ� �۵���
        
        return false;
    }
} */