import * as THREE from './three.module.js';
import { OrbitControls } from './OrbitControls.js';
import { GLTFLoader } from './GLTFLoader.js';


window.onload = function() {
    new App();
}

class App {
    constructor() 
    {
        const divContainer = document.querySelector("#webgl-container");
        this._divContainer = divContainer;

        const renderer = new THREE.WebGLRenderer({antialias: true});
        renderer.setPixelRatio(window.devicePixelRatio);
        divContainer.appendChild(renderer.domElement);
        this._renderer = renderer;

        console.log("renderer:"+renderer);
        
        const scene = new THREE.Scene();
        scene.background = new THREE.Color(0x00ff00);
        this._scene = scene;

        this._setupCamera();
        this._setupLight();
        this._setupModel();
        this._setupControls();

        console.log("-------");
        window.onresize = this.resize.bind(this);
        this.resize();

        requestAnimationFrame(this.render.bind(this));
    }

    _setupModel(){
        /*
        new GLTFLoader().load('../gltf/scene.gltf' , (gltf) => {
            const model = gltf.scene;
            model.position.set(-1,0,0);
            this._scene.add(model);

            this._setupAnimations(gltf);
        });
*/
        new GLTFLoader().load('/gltf/scene4.gltf' , (gltf1) => {
            const model1 = gltf1.scene;
            model1.position.set(1,0,0);
            this._scene.add(model1);

            this._setupAnimations1(gltf1);
        });

        new GLTFLoader().load('/gltf/scene.gltf' , (gltf2) => {
            const model2 = gltf2.scene;
            model2.position.set(0,0,1);
            this._scene.add(model2);
            
            this._setupAnimations2(gltf2);
        });
    }

    _setupAnimations(gltf){
        const model = gltf.scene;
        console.log("model1 : "+model);
        const mixer = new THREE.AnimationMixer(model);
        const gltfAnimaions = gltf.animations;
        const animationsMap = {};

        gltfAnimaions.forEach(AnimationClip => {
            const name = AnimationClip.name;
            console.log("name:"+ name);

            const animationAction = mixer.clipAction(AnimationClip);
            animationsMap[name] = animationAction;
            console.log("animationsMap_"+animationsMap[name]);
            //
            //const previousAnimationAction = this._currentAnimationAction;
            
            //this._currentAnimationAction = this._animationsMap["Take 001"];

            //if(previousAnimationAction !== this._currentAnimationAction)
            //{
                //previousAnimationAction.fadeOut(0.5);
                //this._currentAnimationAction.reset().fadeOut(0.5).play();
            //}
        });

        this._mixer = mixer;
        this._animationsMap = animationsMap;
        this._currentAnimationAction = this._animationsMap["Take 001"];
        this._currentAnimationAction.play();

    }

    _setupAnimations1(gltf){
        console.log("gltf_name:"+gltf);
        const model = gltf.scene;
        const mixer = new THREE.AnimationMixer(model);
        const gltfAnimaions = gltf.animations;
        const animationsMap = {};

        gltfAnimaions.forEach(AnimationClip => {
            const name = AnimationClip.name;
            console.log("name:"+ name);

            const animationAction = mixer.clipAction(AnimationClip);
            animationsMap[name] = animationAction;
            console.log("animationsMap_"+animationsMap[name]);
            //
            //const previousAnimationAction = this._currentAnimationAction;
            
            //this._currentAnimationAction = this._animationsMap["Take 001"];

            //if(previousAnimationAction !== this._currentAnimationAction)
            //{
                //previousAnimationAction.fadeOut(0.5);
                //this._currentAnimationAction.reset().fadeOut(0.5).play();
            //}
        });

        this._mixer1 = mixer;
        this._animationsMap = animationsMap;
        this._currentAnimationAction1 = this._animationsMap["Take 001"];
        this._currentAnimationAction1.play();

    }

    _setupAnimations2(gltf){
        const model = gltf.scene;
        const mixer = new THREE.AnimationMixer(model);
        const gltfAnimaions = gltf.animations;
        const animationsMap = {};

        gltfAnimaions.forEach(AnimationClip => {
            const name = AnimationClip.name;
            console.log("name:"+ name);

            const animationAction = mixer.clipAction(AnimationClip);
            animationsMap[name] = animationAction;
            console.log("animationsMap_"+animationsMap[name]);
            //
            //const previousAnimationAction = this._currentAnimationAction;
            
            //this._currentAnimationAction = this._animationsMap["Take 001"];

            //if(previousAnimationAction !== this._currentAnimationAction)
            //{
                //previousAnimationAction.fadeOut(0.5);
                //this._currentAnimationAction.reset().fadeOut(0.5).play();
            //}
        });

        this._mixer2 = mixer;
        this._animationsMap = animationsMap;
        this._currentAnimationAction = this._animationsMap["Armature|run_cycle"];
        this._currentAnimationAction.play();

    }

    _setupCamera()
    {
        const width  = this._divContainer.clientWidth;
        const height = this._divContainer.clientHeight;
        const camera = new THREE.PerspectiveCamera(
            100,
            width/height,
            1,
            1000
        );
        
        camera.position.z = 1000;
        camera.position.y = 50;
        camera.position.x = 10;
        console.log("_"+camera.position);
        this._camera = camera;
    }

    _setupLight() 
    {
        console.log("setupLight");
        const color = 0xffffff;
        const intensity = 3;
        const light = new THREE.DirectionalLight(color, intensity);
        light.position.set(-1,12,4);
        this._scene.add(light);
    }

    resize() {
        const width  = this._divContainer.clientWidth;
        const height = this._divContainer.clientHeight;

        this._camera.aspect = width/height;
        this._camera.updateProjectionMatrix();
        this._renderer.setSize(width, height);
    }

    _setupControls()
    {
        new OrbitControls(this._camera, this._divContainer);
    }

    render(time)
    {
        this._renderer.render(this._scene, this._camera);
        //console.log("render:"+time);
        this.update(time);
        requestAnimationFrame(this.render.bind(this));
    }

    update(time)
    {
        //console.log("update:"+time);
        time *= 0.001;
        if(this._mixer) {
            const deltaTime = time - this._previousTime;
            this._mixer.update(deltaTime);
        }

        if(this._mixer1) {
            const deltaTime = time - this._previousTime;
            this._mixer1.update(deltaTime);
        }

        if(this._mixer2) {
            const deltaTime = time - this._previousTime;
            this._mixer2.update(deltaTime);
        }

        this._previousTime = time;
    }
}