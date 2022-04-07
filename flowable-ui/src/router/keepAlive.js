
/**
 * 要缓存的组件，路由和组件都需要填加name属性，并且要相同
 * 从 'KeepAliveDemo' 跳转到 'DatePickerDemo'会被缓存，其他情况不缓存
 */
export var keep = [
  {
    name: 'YCTISPCT100View',
    to: ['PCT100ThresholdSetting']
  },
  {
    name: 'YCTCCB03View',
    to: ['CCB03ThresholdSetting']
  },
  {
    name: 'YCTISPCT200View',
    to: ['PCT200ThresholdSetting']
  },
  {
    name: 'YCTISPCT200View',
    to: ['PCT200DeviceChannelTimeConfig']
  },
  {
    name: 'YCTNJ01View',
    to: ['NJ01ThresholdSetting']
  },
  {
    name: 'YCTNJ01View',
    to: ['NJ01DeviceChannelTimeConfig']
  },
  {
    name: 'Terminal',
    to: ['YCTISPCT200View', 'YCTISPCT100View', 'YCTCCB03View', 'YCTBH03View', 'YCTNJ01View']
  },
  {
    name: 'DeviceRelate',
    to: ['ShowCamera']
  },
  {
    name: 'AlarmRecord',
    to: ['AlarmStateHis']
  },
  {
    name: 'CameraInfo',
    to: ['DetectRecord', 'RefPicList']
  }

]
