package com.Teamic.SantaSlay.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.Teamic.SantaSlay.SSGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(720, 480);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new SSGame();
        }
        
        
}