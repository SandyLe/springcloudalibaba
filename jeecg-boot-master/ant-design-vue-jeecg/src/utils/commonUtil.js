import { getAreaOne } from '@/api/api'

export function getFullAddress (provinceId, cityId, districtId,address){
  let fullAddress = '';
  getAreaOne({id:districtId}).then((res) => {
    if (res.success && res.result) {
      fullAddress = fullAddress + res.result.name;
      console.log(fullAddress)
    }
  })
  getAreaOne({id:cityId}).then((res) => {
    if (res.success && res.result) {
      fullAddress = fullAddress + res.result.name;
      console.log(fullAddress)
    }
  })
  getAreaOne({id:provinceId}).then((res) => {
    if (res.success && res.result) {
      fullAddress = fullAddress + res.result.name;
      console.log(fullAddress)
    }
  })

  return fullAddress + address;
}

export default {
  getFullAddress
}
