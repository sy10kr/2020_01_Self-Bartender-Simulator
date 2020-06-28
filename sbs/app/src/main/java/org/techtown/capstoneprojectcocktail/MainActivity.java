package org.techtown.capstoneprojectcocktail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.capstoneprojectcocktail.ui.myPage.MyPageFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_PROFILE = 264;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    NavController navController;
    public FirebaseFirestore db;
    private MenuItem logInItem;
    private MenuItem logOutItem;
    private TextView navUserNameTextView;
    private ImageView navUserProfilePictureImageView;
    private boolean signInCheckForMyPage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_myPage).setDrawerLayout(drawerLayout).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        logInItem = navigationView.getMenu().findItem(R.id.nav_signInString);
        logOutItem = navigationView.getMenu().findItem(R.id.nav_signOutString);

        View headerView = navigationView.getHeaderView(0);
        navUserNameTextView = headerView.findViewById(R.id.userNameText_nav);
        navUserProfilePictureImageView = headerView.findViewById(R.id.profileImageView_nav);

        /*
        View headerForMypage = getLayoutInflater().inflate(R.layout.fragment_mypage, null, false);
        myPageUserNameTextView = (TextView) headerForMypage.findViewById(R.id.userNameText_myPage);
        myPageUserProfilePictureImageView = (ImageView) headerForMypage.findViewById(R.id.profileImageView_myPage);
         */
        /*
        Fragment frg = null;
        frg = getSupportFragmentManager().findFragmentById(R.id.nav_myPage);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
        */
        //실험용
        //네비게이션바 이름변경, Sign in, Sign out check
        /*
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.userNameText_nav);
        navUsername.setText("Your Text Here");
         */

        //Menu menuView = navigationView.getMenu();
        //MenuItem logoutItem = menuView.findItem(R.id.nav_signInCheck);
        //logoutItem.setVisible(false);
        //navigationView.getMenu().findItem(R.id.nav_signInString).setVisible(false);
        //setup();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
            case R.id.nav_myPage:
                NavigationUI.onNavDestinationSelected(menuItem, navController);
                break;
            case R.id.nav_signInString:
                //Intent intent = new Intent(this, LoginPageActivity.class);
                //startActivity(intent);
                signIn();
                break;
            case R.id.nav_signOutString:
                signOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                //Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
                Toast.makeText(getApplicationContext(),"로그인 실패! 다시 시도해주세요.",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            //Log.e(TAG, "user getEmail : " + user.getEmail());
                            //Log.e(TAG, "user getDisplayName : " + user.getDisplayName());
                            //Log.e(TAG, "user getUid : " + user.getUid());
                            //Log.e(TAG, "user getPhoto : " + user.getPhotoUrl());
                            Toast.makeText(getApplicationContext(),"로그인 성공!",Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Toast.makeText(GoogleSignInActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),"로그인 실패! 다시 시도해주세요.",Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut(); // Firebase sign out

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();
                        updateUI(null);
                    }
                });
        Toast.makeText(getApplicationContext(),"로그아웃",Toast.LENGTH_LONG).show();
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(final FirebaseUser currentUser) {
        //hideProgressDialog();
        //유저가 로그인한 경우
        /*
        MyPageFragment myPageFragment = (MyPageFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        try{
            myPageFragment.updateUIForMyPage(currentUser);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        */
        if (currentUser != null) {
            logInItem.setVisible(false);
            logOutItem.setVisible(true);
            final Bitmap[] bitmap = new Bitmap[1];
            Thread mThread= new Thread(){
                @Override
                public void run() {
                    try{
                        URL url = new URL(currentUser.getPhotoUrl().toString());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap[0] = BitmapFactory.decodeStream(is);
                    } catch (MalformedURLException ee) {
                        ee.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            };
            mThread.start();
            try{
                mThread.join();
                navUserProfilePictureImageView.setImageBitmap(bitmap[0]);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            navUserNameTextView.setText(currentUser.getDisplayName());
            signInCheckForMyPage = true;
        }
        //유저가 로그인 하지 않은 경우
        else {
            logInItem.setVisible(true);
            logOutItem.setVisible(false);
            navUserProfilePictureImageView.setImageResource(R.mipmap.ic_launcher_round);
            navUserNameTextView.setText("Unknown");
            signInCheckForMyPage = false;
        }
    }

    public boolean isSignInCheckForMyPage() { return signInCheckForMyPage; }
}
