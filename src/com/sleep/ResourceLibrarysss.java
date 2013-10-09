package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class ResourceLibrarysss {		
		public static Animation playerIdleAnimation() {
			Array<TextureRegion> anim = new Array<TextureRegion>();
			anim.add(new TextureRegion(new Texture (Gdx.files.internal("images/player.png"))));
			anim.add(new TextureRegion(new Texture (Gdx.files.internal("images/player_bw.png"))));
			return new Animation(1000, anim);
		}
		
		public static Texture grid() {
			Texture tex = new Texture(Gdx.files.internal("images/grid.png"));
			tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
			return tex;
		}
		
		public static Texture box() {
			return new Texture(Gdx.files.internal("images/box.png"));
		}
		
		public static Texture wall() {
			return new Texture(Gdx.files.internal("images/wall.png"));
		}
		
		public static Texture enemy() {
			return new Texture(Gdx.files.internal("images/enemy.png"));
		}
}
