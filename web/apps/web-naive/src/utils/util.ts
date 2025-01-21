export function listToTree(list:any) {
  // 创建一个 Map 来保存所有的节点，以便按 id 查找
  const map = new Map();
  const result:any = [];

  // 1. 遍历所有的节点，构建 map
  list.forEach((item:any) => {
    map.set(item.id, { ...item });
  });

  // 2. 遍历节点并构建树
  list.forEach((item :any)=> {
    if (item.parentId === null || item.parentId === 0||item.parentId==="0") {
      // 如果 parentId 是 null 或 0，说明是根节点
      result.push(map.get(item.id));
    } else {
      // 如果有父节点，将当前节点添加到父节点的 children 中
      const parent = map.get(item.parentId);
      if (parent) {

        if(!parent.children){
          parent.children=[];
        }
        parent.children.push(map.get(item.id));
      }
    }
  });

  return result;
}


