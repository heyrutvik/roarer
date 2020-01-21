<template>
        <v-content>
            <v-card class="mx-auto mt-50" max-width="500" height="300" align="center" justify="center">
                <v-card-text>
                    <p class="display-1 text--primary">
                        {{welcomeTxt}}
                    </p>
                    <div class="text--primary" v-html="tagline"></div>
                </v-card-text>
                <v-card-actions class="justify-center"> 
                    <v-btn x-large color="primary" class="mt-50" dark @click="goto">
                        Login with Twitter <v-icon right dark>mdi-twitter</v-icon>
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-content>
</template>

<script>
import api from '@apis/api'

export default {
    name: 'Login',
    data(){
        return {
            welcomeTxt: "Welcome to Roarer",
            tagline: "There are no secrets to success. <br/> It is the result of preparation, hard work, and learning from failure.",
        }
    },
    methods: {
        goto(){
            window.location = api.twitterInit()
        }
    }
}
</script>

<style>
.mt-50{margin-top:50px}
</style>