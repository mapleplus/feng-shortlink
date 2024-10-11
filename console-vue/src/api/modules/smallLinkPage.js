import http from '../axios'
export default {
  queryPage(data) {
    return http({
      url: '/shortlink',
      method: 'get',
      params: data
    })
  },
  addSmallLink(data) {
    return http({
      url: '/shortlink',
      method: 'post',
      data
    })
  },
  addLinks(data) {
    return http({
      responseType: 'arraybuffer',
      url: '/create/batch',
      method: 'post',
      data,
      // responseType: 'blob'
    })
  },
  editSmallLink(data) {
    return http({
      url: '/shortlink/update',
      method: 'post',
      data
    })
  },
  // 通过链接查询标题
  queryTitle(data) {
    return http({
      method: 'get',
      url: '/title',
      params: data
    })
  },
  // 移动到回收站
  toRecycleBin(data) {
    return http({
      url: '/shortlink/recycle-bin',
      method: 'post',
      data
    })
  },
  // 查询回收站数据
  queryRecycleBin(data) {
    return http({
      url: '/shortlink/recycle-bin',
      method: 'get',
      params: data
    })
  },
  // 恢复短链接
  recoverLink(data) {
    return http({
      method: 'post',
      url: '/shortlink/recycle-bin/recover',
      data
    })
  },
  removeLink(data) {
    return http({
      method: 'post',
      url: '/shortlink/recycle-bin/remove',
      data
    })
  },
  // 查询单链的图表数据
  queryLinkStats(data) {
    return http({
      method: 'get',
      params: data,
      url: 'stats'
    })
  },
  // 查询分组的访问记录
  queryLinkTable(data) {
    return http({
      method: 'get',
      params: data,
      url: 'stats/page'
    })
  }
}
