import defaultSettings from './setting'

const title = defaultSettings.title || '无头CMS内容管理系统'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
