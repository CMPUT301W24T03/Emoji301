package com.example.emojibrite;

import static org.junit.Assert.assertEquals;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FragmentTest {

    @Test
    public void loginNavGraphTest() {
        // Create a TestNavHostController
        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());

        // Create a graphical FragmentScenario for the NameScreenFragment
        FragmentScenario<NameScreenFragment>  nameScreenScenario = FragmentScenario.launchInContainer(NameScreenFragment.class);

        nameScreenScenario.onFragment(fragment -> {
            // Set the graph on the TestNavHostController
            navController.setGraph(R.navigation.login_nav_graph);

            //Make the NavController available via the findNavController() API
            Navigation.setViewNavController(fragment.requireView(), navController);

            // Verify that performing a click changes the NavController's state
            fragment.getView().findViewById(R.id.nameScreenBackNext).performClick();
            assertEquals(navController.getCurrentDestination().getId(), R.id.uploadImageScreen);
        });

        // Create a graphical FragmentScenario for the UploadImageScreenFragment
        FragmentScenario<UploadImageScreenFragment> uploadImageScreenScenario = FragmentScenario.launchInContainer(UploadImageScreenFragment.class);

        uploadImageScreenScenario.onFragment(fragment -> {
           // Make the NavController available via the findNavController() API
           Navigation.setViewNavController(fragment.requireView(), navController);

           // Verify that perfoming a click changes the NacVontroller's State
            fragment.getView().findViewById(R.id.uploadImageScreenNext).performClick();
            assertEquals(navController.getCurrentDestination().getId(), R.id.previewScreen);
        });
    }
}
