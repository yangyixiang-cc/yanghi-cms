import {
  post,
  get,
  _delete
} from '@/utils/request';

export default class Tool {
  /**
   * 文件上传
   * @param file
   * @returns
   */
  static async uploadFile(file) {
    return post('/cos/upload',
      file,
    );
  }

  static async deleteFile(filePath) {
    return _delete('/cos/delete', {
      fileName: filePath
    })
  }

  //服务监控
  static async serverMonitor() {
    return post('/monitor/server')
  }


}
