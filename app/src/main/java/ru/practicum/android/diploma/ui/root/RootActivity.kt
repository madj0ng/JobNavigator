package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodel.FilterViewModel

class RootActivity : AppCompatActivity() {
    private val filterViewModel: FilterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val bottomNavigationGroup = findViewById<Group>(R.id.bottomNavigationGroup)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFiltersFragment -> bottomNavigationGroup.visibility = View.GONE

                R.id.jobDetailsFragment -> bottomNavigationGroup.visibility = View.GONE

                R.id.searchFiltersFragment -> bottomNavigationGroup.visibility = View.GONE

                R.id.countryChooseFragment -> bottomNavigationGroup.visibility = View.GONE

                R.id.industryChooseFragment -> bottomNavigationGroup.visibility = View.GONE

                R.id.placeOfWorkFragment -> bottomNavigationGroup.visibility = View.GONE

                R.id.searchFiltersRegionFragment -> bottomNavigationGroup.visibility = View.GONE

                R.id.searchFiltersCityFragment -> bottomNavigationGroup.visibility = View.GONE

                else -> {
                    bottomNavigationGroup.visibility = View.VISIBLE
                }
            }
        }

        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }
}
