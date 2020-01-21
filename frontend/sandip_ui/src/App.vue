<template>
  <v-app>
        <!--header bar-->
        <v-app-bar app color="primary" dark>
            <div class="d-flex align-center">
                <h1>{{company}}</h1>   
            </div>
            <v-spacer></v-spacer>
            <h4 v-if="isOnLine">{{username}}</h4>
            <v-menu left bottom v-if="isOnLine">
                  <template v-slot:activator="{ on }">
                    <v-btn icon v-on="on">
                        <v-icon>mdi-dots-vertical</v-icon>
                    </v-btn>
                  </template>
                  <v-list>
                      <v-list-item @click="logout">
                          <v-list-item-title >Logout</v-list-item-title>
                      </v-list-item>
                  </v-list>
            </v-menu>
        </v-app-bar>
        <!--end-->

        <!--all dynamic component will loaded here-->
        <router-view></router-view>
        <!--end-->

        <!--messages will be display here-->
        <v-snackbar
            v-model="snackbar"
            :color="snackbarColor"
            multi-line
            dark
            bottom
            >
            {{ snackbarText }}
            <v-btn text @click="snackbar = false">
                Close
            </v-btn>
        </v-snackbar>
        <!--end-->
    </v-app>
</template>

<script>
import api from '@apis/api'

export default {
  name: 'App',
  data(){
    return {
      company: "Roarer",
      username: "Welcome",
      snackbar: false,
      error: false,
      snackbarText: 'Something went wrong.',
      online: false
    }
  },
  computed: {
      snackbarColor() {
          return this.error ? 'error' : 'success'
      },
      isOnLine(){
        return this.online
      }
  },
  methods:{
    logout(){
        api.logout()
        .then(()=>{
            api.removeCookie(api.cookieName)
            this.online = false
            this.$router.push({name: 'login'})
        })
        .catch((error) => {
            this.snackbarText = error.response.statusText
            this.snackbar = true
            this.error = true
        })
        .finally(() => {
            this.snackbarText = "You have logged out successfully"
            this.snackbar = true
            this.error = false
        })
    }
  },
  created(){
    this.online = api.isLogin()
  }
};
</script>
