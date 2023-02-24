import userStore from "./modules/user";
import permissionsStore from "./modules/permission";
export default function useStore() {
  return {
    user: userStore(),
    permissions: permissionsStore()
  }
}
