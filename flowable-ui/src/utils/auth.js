import Cookies from 'js-cookie'
import defaultSettings from '@/settings'

export function getToken() {
  var token = Cookies.get(defaultSettings.name)
  if (token) { setToken(token) }
  return token
}

export function setToken(token) {
  return Cookies.set(defaultSettings.name, token, { expires: 1 })
}

export function removeToken() {
  return Cookies.remove(defaultSettings.name)
}
