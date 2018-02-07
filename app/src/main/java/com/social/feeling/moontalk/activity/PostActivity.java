package com.social.feeling.moontalk.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.empire.vmd.client.android_lib.activity.BaseFragmentActivity;
import com.empire.vmd.client.android_lib.adapter.BaseTypeAdapter;
import com.empire.vmd.client.android_lib.component.CompleteHookComponent;
import com.empire.vmd.client.android_lib.util.AndroidBuiltInUtil;
import com.empire.vmd.client.android_lib.util.DateTimeUtil;
import com.empire.vmd.client.android_lib.util.EffectUtil;
import com.empire.vmd.client.android_lib.util.FileUtil;
import com.empire.vmd.client.android_lib.util.OtherUtil;
import com.social.feeling.moontalk.R;
import com.social.feeling.moontalk.fragment.GalleryFragment;
import com.social.feeling.moontalk.fragment.TagsFragment;
import com.social.feeling.moontalk.global.Feelings;
import com.social.feeling.moontalk.global.FileConfig;
import com.social.feeling.moontalk.global.LoginData;
import com.social.feeling.moontalk.global.MainController;
import com.social.feeling.moontalk.global.PostFeeling;
import com.social.feeling.moontalk.item.NicknameItem;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends BaseFragmentActivity {
    private static final int DURATION = 1000;
    private static final int HISTORY_NICKNAME_MAX_NUM = 10;
    private static final int ACTIVITY_NUM = 0;
    private static final String HISTORY_NICKNAME_FILE_NAME = "history_nickname.txt";
    private TextView tvQuote;
    private TextView tvCommit;
    private TextView tvCancel;
    private TextView tvAnonymous;
    private TextView tvPost;
    private TextView tvShare;
    private TextView tvEnterNickname;
    private TextView tvHistoryNickname;
    private TextView tvEmptyHistoryNickname;
    private EditText etThought;
    private EditText etNickname;
    private RelativeLayout rlComplete;
    private LinearLayout llPostOptions;
    private LinearLayout llEnterNickname;
    private RelativeLayout rlHistoryNickname;
    private LinearLayout llAnonymous;
    private ListView lvHistoryNickname;
    private GalleryFragment imagesFragment;
    private TagsFragment tagsFragment;
    private EffectUtil effectUtil;
    private PostFeeling postFeeling = PostFeeling.getInstance();
    private LoginData loginData = LoginData.getInstance(this);
    private List<String> historyNicknameList;
    private FileUtil fileUtil;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postFeeling.addActivity(this);
        fileUtil = new FileUtil(PostActivity.this);
        findViews();
        effectUtil = new EffectUtil();
        initPhotos();
        tvQuote.setText(postFeeling.feeling.quote);
        //initFlTags();
        tvCommit.setOnClickListener(getLlPostOptionAppearListener());
        tvCancel.setOnClickListener(getFinishListener());
        setLlPostOptionsListeners();
        setLlAnonymousListeners();
    }

    private void findViews() {
        llPostOptions = (LinearLayout) findViewById(R.id.llPostOptions);
        llAnonymous = (LinearLayout) findViewById(R.id.llAnonymous);
        llEnterNickname = (LinearLayout) findViewById(R.id.llEnterNickname);
        rlHistoryNickname = (RelativeLayout) findViewById(R.id.rlHistoryNickname);
        tvQuote = (TextView) findViewById(R.id.tvQuote);
        tvCommit = (TextView) findViewById(R.id.tvCommit);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvAnonymous = (TextView) findViewById(R.id.tvAnonymous);
        tvPost = (TextView) findViewById(R.id.tvPost);
        tvShare = (TextView) findViewById(R.id.tvShare);
        tvEnterNickname = (TextView) findViewById(R.id.tvEnterNickname);
        tvHistoryNickname = (TextView) findViewById(R.id.tvHistoryNickname);
        tvEmptyHistoryNickname = (TextView) findViewById(R.id.tvEmptyHistoryNickname);
        etThought = (EditText) findViewById(R.id.etThought);
        etNickname = (EditText) findViewById(R.id.etNickname);
        rlComplete = (RelativeLayout) findViewById(R.id.rlComplete);
        lvHistoryNickname = (ListView) findViewById(R.id.lvHistoryNickname);
    }

    private void initPhotos() {
        imagesFragment = new GalleryFragment(this, postFeeling.feeling.checkedPhotoList, GalleryFragment.HORIZONTAL);
        getSupportFragmentManager().beginTransaction().replace(R.id.flPhotos, imagesFragment).commit();
    }

//    private void initFlTags() {
//        tagsFragment = new TagsFragment(this, postFeeling.feeling.tagList);
//        getSupportFragmentManager().beginTransaction().replace(R.id.flTags, tagsFragment).commit();
//    }

    private View.OnClickListener getFinishListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    private View.OnClickListener getLlPostOptionAppearListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAppear(llPostOptions);
                postFeeling.feeling.thought = etThought.getText().toString();
                tvCancel.setOnClickListener(getLlPostOptionDisappearListener());
                etThought.setEnabled(false);
            }
        };
    }

    private View.OnClickListener getNewNicknamePostListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNickname = etNickname.getText().toString();

                if (newNickname.isEmpty()) {
                    Toast.makeText(PostActivity.this, R.string.no_enter_nickname, Toast.LENGTH_SHORT).show();
                } else {
                    postByNickname(newNickname);
                    saveNickname(newNickname);
                    new AndroidBuiltInUtil(PostActivity.this).toggleKeyboard(llAnonymous, false);
                }
            }
        };
    }

    private void postByNickname(String nickname) {
        postFeeling.feeling.account = loginData.personData.account;
        postFeeling.feeling.name = nickname;
        postFeeling.feeling.id = DateTimeUtil.getNowString("yyyyMMdd-HHmmss");
        Feelings.postFeeling(this, postFeeling.feeling);
        postSuccess();
    }

    private void saveNickname(String newNickname) {
        getHistoryNicknameList();
        historyNicknameList = (historyNicknameList == null) ? new ArrayList<String>() : historyNicknameList;
        if (historyNicknameList.size() == HISTORY_NICKNAME_MAX_NUM) {
            historyNicknameList.remove(HISTORY_NICKNAME_MAX_NUM - 1);
        } else if (historyNicknameList.size() == 0) {
            historyNicknameList.add(newNickname);
        } else {
            historyNicknameList.add(0, newNickname);
        }
        fileUtil.saveExternalFile(FileConfig.EXTERNAL_DIR, HISTORY_NICKNAME_FILE_NAME, getNewStrNicknameList());
    }

    private void getHistoryNicknameList() {
        String strNicknames = fileUtil.getStringFromExternalFile(FileConfig.EXTERNAL_DIR, HISTORY_NICKNAME_FILE_NAME);

        if (strNicknames != null && !strNicknames.isEmpty()) {
            String[] nicknames = strNicknames.split(",");

            historyNicknameList = new OtherUtil().getStringListFromArray(nicknames);
        }
    }

    private String getNewStrNicknameList() {
        String result = "";

        for (int i = 0; i < historyNicknameList.size(); i++) {
            String nickname = historyNicknameList.get(i);

            result += nickname;
            result += (i < historyNicknameList.size() - 1) ? "," : "";
        }

        return result;
    }

    private View.OnClickListener getLlAnonymousAppearListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAppear(llAnonymous);
                tvCancel.setOnClickListener(getLlAnonymousDisappearListener());
                etThought.setEnabled(false);
            }
        };
    }

    private void viewAppear(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
            effectUtil.activateViewAnimations(view, getAppearAnimations(), DURATION);
            etThought.setEnabled(false);
        }
    }

    private View.OnClickListener getLlPostOptionDisappearListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDisappear(llPostOptions);
                tvCancel.setOnClickListener(getFinishListener());
                etThought.setEnabled(true);
            }
        };
    }

    private View.OnClickListener getLlAnonymousDisappearListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDisappear(llAnonymous);
                tvCancel.setOnClickListener(getFinishListener());
                tvCommit.setOnClickListener(getLlPostOptionAppearListener());
                etThought.setEnabled(true);
            }
        };
    }

    private void viewDisappear(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            effectUtil.activateViewAnimations(view, getDisappearAnimations(), DURATION);
            view.setVisibility(View.GONE);
        }
        llEnterNickname.setVisibility(View.GONE);
        etNickname.setText("");
        rlHistoryNickname.setVisibility(View.GONE);
    }

    private Animation[] getAppearAnimations() {
        Animation[] result = new Animation[2];

        result[0] = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 1f
                , Animation.RELATIVE_TO_SELF, 0f);
        result[1] = new AlphaAnimation(0, 1);

        return result;
    }

    private Animation[] getDisappearAnimations() {
        Animation[] result = new Animation[2];

        result[0] = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 1f);
        result[1] = new AlphaAnimation(1, 0);

        return result;
    }

    private void setLlPostOptionsListeners() {
        tvAnonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDisappear(llPostOptions);
                viewAppear(llAnonymous);
                tvCommit.setOnClickListener(getLlAnonymousAppearListener());
                tvCancel.setOnClickListener(getLlAnonymousDisappearListener());
                etThought.setEnabled(false);
            }
        });
        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postFeeling.feeling.account = loginData.personData.account;
                postFeeling.feeling.name = loginData.personData.name;
                postFeeling.feeling.photoUri = loginData.personData.photoUrl;
                postFeeling.feeling.id = DateTimeUtil.getNowString("yyyyMMdd-HHmmss");
                Feelings.postFeeling(PostActivity.this, postFeeling.feeling);
                postSuccess();
            }
        });
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCommit.setOnClickListener(getLlPostOptionAppearListener());
                tvCancel.setOnClickListener(getFinishListener());
                viewDisappear(llPostOptions);
                startActivityForResult(new Intent(PostActivity.this, SharePostActivity.class), ACTIVITY_NUM);
                etThought.setEnabled(true);
            }
        });
    }

    private void setLlAnonymousListeners() {
        tvEnterNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlHistoryNickname.setVisibility(View.GONE);
                llEnterNickname.setVisibility(View.VISIBLE);
                tvCommit.setOnClickListener(getNewNicknamePostListener());
            }
        });
        tvHistoryNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llEnterNickname.setVisibility(View.GONE);
                rlHistoryNickname.setVisibility(View.VISIBLE);
                getHistoryNicknameList();
                if (historyNicknameList != null && historyNicknameList.size() > 0) {
                    tvEmptyHistoryNickname.setText("");
                    BaseTypeAdapter.IItemView iItemView = new BaseTypeAdapter.IItemView() {
                        @Override
                        public View getItemView(int position, View convertView) {
                            return new NicknameItem(PostActivity.this, historyNicknameList.get(position)).getView();
                        }
                    };
                    BaseTypeAdapter adapter = new BaseTypeAdapter(iItemView, historyNicknameList);

                    lvHistoryNickname.setAdapter(adapter);
                    lvHistoryNickname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            postByNickname(historyNicknameList.get(position));
                        }
                    });
                } else {
                    tvEmptyHistoryNickname.setText(R.string.empty_history_nickname);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_NUM && resultCode == RESULT_OK) {
            postSuccess();
        }
    }

    private void postSuccess() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                MainController mainController = MainController.getInstance();

                postFeeling.clear();
                if (mainController != null) {
                    //mainController.setFragmentIndex(MainController.FEELING_POST);
                    mainController.refreshFeelingPost();
                }
            }
        };
        rlComplete.addView(new CompleteHookComponent(this, getResources().getString(R.string.post_success), handler).getView());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postFeeling.feeling.thought = null;
        postFeeling.removeActivity(this);
    }
}
