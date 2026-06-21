<script setup>
import { computed, nextTick, onBeforeUnmount, onMounted, ref } from 'vue'
import { createChatStreamUrl } from './api/chat'

const STORAGE_KEY = 'ai-code-helper-memory-id'

const messages = ref([])
const inputValue = ref('')
const isStreaming = ref(false)
const errorText = ref('')
const chatBodyRef = ref(null)
const activeSource = ref(null)

const memoryId = ref(createMemoryId())

const canSend = computed(() => inputValue.value.trim().length > 0 && !isStreaming.value)

onMounted(() => {
  messages.value.push({
    id: crypto.randomUUID(),
    role: 'assistant',
    content: '你好，我是 AI 小助手。可以问我编程学习、项目实践、简历优化或面试准备相关的问题。'
  })
  scrollToBottom()
})

onBeforeUnmount(() => {
  closeStream()
})

function createMemoryId() {
  const cached = Number(localStorage.getItem(STORAGE_KEY))
  if (Number.isInteger(cached) && cached > 0) {
    return cached
  }

  const id = Math.floor(Date.now() % 1000000000)
  localStorage.setItem(STORAGE_KEY, String(id))
  return id
}

function resetChat() {
  closeStream()
  const nextId = Math.floor(Date.now() % 1000000000)
  localStorage.setItem(STORAGE_KEY, String(nextId))
  memoryId.value = nextId
  errorText.value = ''
  inputValue.value = ''
  messages.value = [
    {
      id: crypto.randomUUID(),
      role: 'assistant',
      content: '新的聊天室已创建。继续告诉我你的问题吧。'
    }
  ]
  scrollToBottom()
}

function sendMessage() {
  const question = inputValue.value.trim()
  if (!question || isStreaming.value) {
    return
  }

  closeStream()
  errorText.value = ''
  inputValue.value = ''

  messages.value.push({
    id: crypto.randomUUID(),
    role: 'user',
    content: question
  })

  const assistantMessage = {
    id: crypto.randomUUID(),
    role: 'assistant',
    content: ''
  }
  messages.value.push(assistantMessage)
  isStreaming.value = true
  scrollToBottom()

  const streamUrl = createChatStreamUrl(memoryId.value, question)
  const source = new EventSource(streamUrl)
  activeSource.value = source

  source.onmessage = (event) => {
    assistantMessage.content += event.data
    scrollToBottom()
  }

  source.onerror = () => {
    if (!assistantMessage.content) {
      assistantMessage.content = '抱歉，暂时没有收到回复。请确认后端服务已经启动。'
    }
    errorText.value = '连接已结束或发生异常。'
    isStreaming.value = false
    closeStream()
    scrollToBottom()
  }
}

function closeStream() {
  if (activeSource.value) {
    activeSource.value.close()
    activeSource.value = null
  }
  isStreaming.value = false
}

function handleKeydown(event) {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault()
    sendMessage()
  }
}

async function scrollToBottom() {
  await nextTick()
  const el = chatBodyRef.value
  if (el) {
    el.scrollTop = el.scrollHeight
  }
}
</script>

<template>
  <main class="chat-page">
    <section class="chat-shell" aria-label="AI 小助手聊天室">
      <header class="chat-header">
        <div>
          <h1>AI 小助手</h1>
          <p>聊天室 ID：{{ memoryId }}</p>
        </div>
        <button class="ghost-button" type="button" @click="resetChat">新会话</button>
      </header>

      <div ref="chatBodyRef" class="chat-body">
        <article
          v-for="message in messages"
          :key="message.id"
          class="message-row"
          :class="message.role === 'user' ? 'message-row-user' : 'message-row-ai'"
        >
          <div class="avatar" aria-hidden="true">
            {{ message.role === 'user' ? '我' : 'AI' }}
          </div>
          <div class="bubble">
            <p v-if="message.content">{{ message.content }}</p>
            <span v-else class="typing">正在思考...</span>
          </div>
        </article>
      </div>

      <footer class="chat-footer">
        <p v-if="errorText" class="status-text">{{ errorText }}</p>
        <div class="composer">
          <textarea
            v-model="inputValue"
            rows="1"
            placeholder="输入你的编程学习或面试问题..."
            @keydown="handleKeydown"
          />
          <button type="button" :disabled="!canSend" @click="sendMessage">
            {{ isStreaming ? '回复中' : '发送' }}
          </button>
        </div>
      </footer>
    </section>
  </main>
</template>
