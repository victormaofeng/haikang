<template>
    <div>
        <h3>标题：</h3> <div>{{title}}</div>
        <h3>介绍：</h3> <div>{{content}}</div>
        <h3 v-show="play1">原图片：</h3> <img :src="src1" class="img" v-if="src != ''" />
        <h3 v-show="play">生成图片：</h3> <img :src="src" class="img" v-if="src != ''" />
        <h3 v-show="play2">目标人物：</h3> <img :src="src2" class="img" v-if="src != ''" />
        

    </div>
</template>

<script>
    export default{
        data(){
            return{
                title: this.$route.query.message.title,
                content: this.$route.query.message.content,
                src: this.$route.query.message.path,
                src1: this.$route.query.message.path1,
                src2: this.$route.query.message.path2,
                play: true,
                play1: true,
                play2: true,
            };
        },
        created(){
            console.log(this.$route.query);
            this.load();
        },
        methods:{
            load(){
                if(this.src == '' || this.src == null){
                        this.play = ! this.play
                } 
                if(this.src1 == '' || this.src1 == null){
                        this.play1 = ! this.play1
                }
                if(this.src2 == '' || this.src2 == null){
                        this.play2 = ! this.play2
                }
                
            }
        }

    }
</script>
