<template>
        <v-container>
            <v-content>
                <v-row justify="space-around">
                    <v-col cols="6">
                        <Post @updateThread="updateThread"></Post>
                    </v-col>
                    <v-col cols="6">
                        <Thread :threads="allThreads" @updateThread="updateThread"></Thread>
                    </v-col>
                </v-row>
            </v-content>
        </v-container>        
</template>

<script>
import Post from '@component/Post'
import Thread from '@component/Thread'

export default{
    name: 'User',
    data(){
        return{
            allThreads: []
        }
    },
    components:{
        Post,
        Thread
    },
    methods: {
        updateThread(threads = []){
            return this.allThreads = threads
        }
    }
}
</script>

<style>
.p-10{padding:10px}
</style>