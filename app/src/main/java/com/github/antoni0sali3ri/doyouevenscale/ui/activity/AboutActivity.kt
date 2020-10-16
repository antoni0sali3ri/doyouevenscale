package com.github.antoni0sali3ri.doyouevenscale.ui.activity

import ca.allanwang.kau.about.AboutActivityBase
import ca.allanwang.kau.about.LibraryIItem
import ca.allanwang.kau.adapters.FastItemThemedAdapter
import ca.allanwang.kau.utils.string
import com.github.antoni0sali3ri.doyouevenscale.BuildConfig
import com.github.antoni0sali3ri.doyouevenscale.R
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.fastadapter.GenericItem

class AboutActivity : AboutActivityBase(R.string::class.java) {

    override fun Configs.buildConfigs() {
        cutoutForeground = resources.getColor(R.color.colorPrimaryVariant)
        cutoutDrawableRes = R.drawable.ic_beamed_eigth
    }

    override fun postInflateMainPage(adapter: FastItemThemedAdapter<GenericItem>) {
        /*
         * I shamelessly took this bit from Frost for Facebook's about section
         */
        val app = Library(
            definedName = "doyouevenscale",
            libraryName = string(R.string.app_name),
            author = string(R.string.app_author),
            libraryWebsite = string(R.string.app_github_url),
            isOpenSource = true,
            libraryDescription = string(R.string.app_description),
            libraryVersion = BuildConfig.VERSION_NAME,
            licenses = setOf(
                License(
                    definedName = "agplv3",
                    licenseName = "GNU AGPL v3",
                    licenseWebsite = "https://www.gnu.org/licenses/agpl-3.0.html",
                    licenseDescription = "",
                    licenseShortDescription = ""
                )
            )
        )
        adapter.add(LibraryIItem(app))
    }
}