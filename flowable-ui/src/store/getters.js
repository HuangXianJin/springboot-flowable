const getters = {
  sidebar: state => state.app.sidebar,
  autoLoading: state => state.app.autoLoading,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  userId: state => state.user.userId,
  permissionRoutes: state => state.permission.routes,
  addRoutes: state => state.permission.addRoutes,
  keep: state => state.app.keep,
  noKeep: state => state.app.noKeep,
  accountType: statue => statue.user.account.accountType,
  account: state => state.user.account,
  menus: state => state.user.menus,
  dict: state => state.dict.dict,
  theme: state => state.theme,
  settings: state => state.settings
}
export default getters
