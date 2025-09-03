<template>
  <div class="transfers-view">
    <div class="container">
      <header class="header">
        <div class="header-content">
          <h1 class="title">Transfers - Tokio Marine</h1>
          <button 
            class="btn-new-transfer"
            @click="openNewTransferModal"
          >
            New Transfer
          </button>
        </div>
      </header>

      <main class="main-content">
        <TransferTable 
          ref="transferTableRef"
          @open-new-transfer-modal="openNewTransferModal"
        />
      </main>
    </div>

    <NewTransferModal 
      ref="newTransferModalRef"
      @transfer-created="onTransferCreated"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import TransferTable from '../components/TransferTable.vue';
import NewTransferModal from '../components/NewTransferModal.vue';

const transferTableRef = ref<InstanceType<typeof TransferTable> | null>(null);
const newTransferModalRef = ref<InstanceType<typeof NewTransferModal> | null>(null);

const openNewTransferModal = () => {
  newTransferModalRef.value?.show();
};

const onTransferCreated = () => {
  transferTableRef.value?.loadTransfers();
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

.transfers-view {
  min-height: 100vh;
  width: 100%;
  background: #F2F2F2;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 40px 20px;
  font-family: 'Inter', sans-serif;
  box-sizing: border-box;
}

.container {
  max-width: 1200px;
  width: 100%;
}

.header {
  background: #fff;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 32px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  margin: 0;
}

.btn-new-transfer {
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 12px;
  padding: 12px 24px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.25s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-new-transfer:hover {
  background-color: #2563eb;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(59, 130, 246, 0.3);
}

/* Responsivo */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 16px;
  }

  .btn-new-transfer {
    width: 100%;
    justify-content: center;
  }

  .title {
    text-align: center;
    font-size: 24px;
  }
}
</style>