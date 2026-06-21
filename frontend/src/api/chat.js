import axios from 'axios'

export const apiClient = axios.create({
  baseURL: '/api',
  timeout: 30000
})

export function createChatStreamUrl(memoryId, message) {
  return apiClient.getUri({
    url: '/ai/chat',
    params: {
      memoryId,
      message
    }
  })
}
