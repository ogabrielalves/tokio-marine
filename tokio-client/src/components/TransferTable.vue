<template>
  <n-card title="Transfers" size="large" :bordered="false">
    <n-alert
      v-if="error"
      title="Erro ao carregar"
      type="error"
      closable
      class="mb-4"
    >
      {{ error }}
      <n-button size="small" @click="loadTransfers(currentPage)">Tentar novamente</n-button>
    </n-alert>

    <n-data-table
      :columns="columns"
      :data="transfers"
      :loading="loading"
      :bordered="false"
      :single-line="false"
      class="mb-4"
    />

     <n-pagination
      v-model:page="currentPage"
      :page-size="pageSize"
      :item-count="totalElements"
      :on-update:page="handlePageChange"
      class="justify-end"
    />

    <div v-if="!loading && transfers.length === 0" class="text-center py-6 text-gray-500">
      <n-empty description="Nenhuma transferência encontrada" />
    </div>
  </n-card>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import type { Transfer, TransferResponse } from '@/types/transfer';
import { TransferService } from '@/services/transferService';

defineEmits<{
  openNewTransferModal: []
}>();

const transfers = ref<Transfer[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);
const currentPage = ref(0);
const totalPages = ref(0);
const totalElements = ref(0);
const pageSize = ref(10);

const columns = [
  { title: 'Source Account', key: 'sourceAccount' },
  { title: 'Destination Account', key: 'destinationAccount' },
  {
    title: 'Amount',
    key: 'amount',
    render: (row: Transfer) => `R$ ${row.amount.toFixed(2)}`
  },
  {
    title: 'Fee',
    key: 'fee',
    render: (row: Transfer) => `R$ ${row.fee.toFixed(2)}`
  },
  {
    title: 'Transfer Date',
    key: 'transferDate',
    render: (row: Transfer) => new Date(row.transferDate).toLocaleDateString()
  },
  {
    title: 'Scheduling Date',
    key: 'schedulingDate',
    render: (row: Transfer) => new Date(row.schedulingDate).toLocaleDateString()
  }
]

const loadTransfers = async (page = 0) => {
  loading.value = true
  error.value = null
  try {
    const response: TransferResponse = await TransferService.getTransfers(page, pageSize.value)
    transfers.value = response.content
    currentPage.value = response.number + 1
    totalPages.value = response.totalPages
    totalElements.value = response.totalElements
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Erro desconhecido'
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page) => {
  loadTransfers(page - 1) 
}

onMounted(() => {
  loadTransfers();
});

defineExpose({
  loadTransfers
});
</script>

<style scoped>
/* Wrapper */
.transfer-table {
  font-family: 'Inter', sans-serif;
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  width: 100%;
  overflow-x: auto;
}


.table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
  font-size: 14px;
}

.table thead {
  background: #f9fafb;
  position: sticky;
  top: 0;
  z-index: 1;
}

.table th {
  padding: 12px 16px;
  font-weight: 600;
  color: #374151;
  text-transform: uppercase;
  font-size: 12px;
  border-bottom: 2px solid #e5e7eb;
}

.table td {
  padding: 12px 16px;
  border-bottom: 1px solid #e5e7eb;
  color: #4b5563;
}

/* Linhas zebrada */
.table tbody tr:nth-child(odd) {
  background: #f9fafb;
}

/* Hover highlight */
.table tbody tr:hover {
  background: #eef2ff;
}

/* Colunas especiais */
.amount {
  font-weight: 600;
  color: #16a34a;
}

.fee {
  color: #dc2626;
}

/* Empty State */
.empty-row {
  text-align: center;
  padding: 20px;
}

.empty-state {
  padding: 20px;
  color: #6b7280;
}

.empty-icon {
  font-size: 28px;
  margin-bottom: 8px;
}

/* Loading State */
.loading-state, .error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
  color: #6b7280;
}

.loading-spinner {
  width: 32px;
  height: 32px;
  border: 4px solid #e5e7eb;
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 12px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Botão de retry */
.retry-btn {
  background: #3b82f6;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 8px;
  margin-top: 12px;
  cursor: pointer;
  transition: background 0.3s;
}

.retry-btn:hover {
  background: #2563eb;
}

/* Paginação */
.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  font-size: 14px;
}

.pagination {
  display: flex;
  gap: 4px;
}

.pagination button {
  border: none;
  background: #f3f4f6;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.pagination button:hover:not(:disabled) {
  background: #3b82f6;
  color: white;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination .active {
  background: #3b82f6;
  color: white;
  font-weight: bold;
}
</style>