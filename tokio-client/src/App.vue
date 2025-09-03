<script setup lang="ts">
import { NConfigProvider, NNotificationProvider } from 'naive-ui'
import TransfersView from './views/TransfersView.vue'
</script>

<template>
  <n-config-provider>
    <n-notification-provider>
      <main>
        <TransfersView />
      </main>
    </n-notification-provider>
  </n-config-provider>
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

body {
  margin: 0;
  padding: 0;
  font-family: 'Inter', sans-serif;
}

#app {
  min-height: 100vh;
}
</style>
