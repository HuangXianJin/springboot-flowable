import { transform } from 'gcoord'

export function coordsToLatLngWithTransform(coords, crsFrom, crsTo) {
  // 坐标转换
  coords = transform(coords, crsFrom, crsTo)
  return new window.L.LatLng(coords[1], coords[0], coords[2])
}

export function createFullScreen(map, options = {}) {
  return window.L.control.fullscreen({
    position: 'bottomright',
    title: '全屏查看',
    titleCancel: '退出全屏'

  }).addTo(map)
}
