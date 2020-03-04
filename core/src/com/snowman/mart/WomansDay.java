package com.snowman.mart;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import org.w3c.dom.Text;

public class WomansDay extends ApplicationAdapter {
	SpriteBatch batch;
	private Flowers[] flowers;
	private static final int FLOWERS_COUNT = 400;
	Texture blueFlowersTexture;
	Texture redFlowersTexture;
	Texture yellowFlowersTexture;
	Texture dayNumberTexture;
	private Vector2 dayNumberCenter; //место появления (в create инициализируем)
	private float globalTime;
	private Vector2 tmp;

	private class	Flowers {
		private Vector2 position;
		private Vector2 speed;
		private float size;
		private float wind;
		private Texture flowersTexture;
		private float color;
		private int rotatedAngle;
		private float time;


		public Flowers() {
			this.position = new Vector2(0,0);
			this.speed = new Vector2(0,0);
			this.flowersTexture = rndFlowerSelect();
			this.init();
		}

		public void init(){
			//Инициализация каждого объекта из массива
			this.position.set(MathUtils.random(0, 1280),-100.0f - MathUtils.random(820.0f));
			this.speed.set(0.0f,MathUtils.random(100.0f)); //100.0f-200.0f
			this.size = 0.1f + MathUtils.random(0.1f); //0.015f - 0.03f
			this.wind = MathUtils.random(-30.0f,30.0f);
			this.color = MathUtils.random(0.9f, 1.0f); //чем больше тем светлее
			this.time = MathUtils.random(0.0f,100.0f); //
		}

		public void render(){

			//место появления, размер, поворот и т. д.
			//через синусоиду что бы картинка немного болталась
			batch.setColor(color,color,color,1.0f);
			batch.draw(flowersTexture,position.x - 430,position.y - 430, 450.f,450.0f, 300, 300,size,size, speed.angle() - rotatedAngle,0,0,300,300,false,false);
			//с вращением через синусоиду (time)
			//batch.draw(flowersTexture,position.x - 430,position.y - 430, 450.f,450.0f, 300, 300,size * (float)Math.sin(time),size, speed.angle() - rotatedAngle,0,0,300,300,false,false);
			batch.setColor(1,1,1,1);
		}

		public void update(float deltaTime){
			//Обработка всех условий движения, столкновений и т д для каждого объекта массива
			rotatedAngle++;
			position.y += speed.y * deltaTime;
			position.x += speed.x * deltaTime;
			speed.x += wind * deltaTime;
			speed.y += 10.0f * deltaTime;
			if(position.y > 820.0f){
				init();
			}
			time += speed.x * deltaTime / 100.0f;

		}
		public Texture rndFlowerSelect(){
			//рандомно добавляет текстуру в поле объекта
			if(MathUtils.random(1,3)== 1){
				return blueFlowersTexture;
			}
			if(MathUtils.random(1,3)== 2){
				return redFlowersTexture;
			}
			else{
				return yellowFlowersTexture;
			}
		}
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		blueFlowersTexture = new Texture("blueflower.png");
		redFlowersTexture = new Texture("redFlower.png");
		yellowFlowersTexture = new Texture("yellowFlower.png");
		dayNumberTexture = new Texture("green8.png");
		dayNumberCenter = new Vector2(0,0);
		tmp = new Vector2(0,0);
		flowers = new Flowers[FLOWERS_COUNT];
		for (int i = 0; i < flowers.length; i++){
			flowers[i] = new Flowers();
		}
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();
		update(deltaTime);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//8ку рисуем здесь

		dayNumberCenter.set(640.0f,360.0f + 10.0f * (float)Math.sin(globalTime * 2.0f));
		batch.draw(dayNumberTexture,dayNumberCenter.x - 300,dayNumberCenter.y - 400,600.0f,700.0f);

		for (int i = 0; i < flowers.length; i++){
			flowers[i].render();
		}

		batch.end();
	}

	public void update(float deltaTime){
		globalTime +=deltaTime;
		for (int i = 0; i < flowers.length; i++){
			flowers[i].update(deltaTime);
			//окружность вокруг которой цветы будут обтекать 8ку
			float rad = 400.f;
			if (flowers[i].position.dst(dayNumberCenter)<rad){
			tmp.set(dayNumberCenter).sub(flowers[i].position).nor().scl(-rad).add(dayNumberCenter);
			flowers[i].position.set(tmp);
			//отталкиваем влево и вправо цветы меняя скорость
			flowers[i].speed.x += Math.signum(flowers[i].position.x-dayNumberCenter.x)*rad*deltaTime;
			}
		}

	}

	@Override
	public void dispose () {
		batch.dispose();
		blueFlowersTexture.dispose();
		redFlowersTexture.dispose();
		yellowFlowersTexture.dispose();
		dayNumberTexture.dispose();
	}
}
