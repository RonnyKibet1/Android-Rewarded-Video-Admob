package info.codestart.admobrewardedvideo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class MainActivity extends Activity implements RewardedVideoAdListener {

    private RewardedVideoAd mRewardedVideoAd;
    private Button mShowAdButton;
    private TextView mCoinsTextView;
    private int currentCoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        //get the button and the textview
        mShowAdButton = (Button)findViewById(R.id.showAdButton);
        mCoinsTextView = (TextView)findViewById(R.id.textView);

        //get current coins from prefs initially
        mCoinsTextView.setText("Coins: " + getCoinsFromPrefs());



        mShowAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRewardedVideoAd();

            }
        });
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "video ad loaded", Toast.LENGTH_SHORT).show();
        //show video when ad is loaded
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }

    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "video ad opened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "video ad started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "video ad closed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this, "onRewarded! currency: " + rewardItem.getType() + "  amount: " +
                rewardItem.getAmount(), Toast.LENGTH_SHORT).show();

        //add the new coins to the saved coins in prefs
        saveCoinsToPrefs(getCoinsFromPrefs() + rewardItem.getAmount());

        //set the coins that for user
        mCoinsTextView.setText("Coins: " + getCoinsFromPrefs());
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "Left application", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "video failed to load", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    private void saveCoinsToPrefs(int amount){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("COINS", amount);
        editor.apply();
    }

    private int getCoinsFromPrefs(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int coins = sharedPref.getInt("COINS", 0);
        return coins;
    }
}
