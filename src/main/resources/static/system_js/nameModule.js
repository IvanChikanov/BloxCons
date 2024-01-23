import { HTMLFather as HTML } from "../system_js/HTMLFather.js";
class NameModule
{
    textData;
    cellId;
    inside;
    unit = [];
    init(cellId, data = null)
    {
        this.cellId = cellId;
        return this.createObject();
    }
    createObject()
    {
        let element = HTML.create("DIV");
        HTML.addStyles(["maxWidthInherit"],[element]);
        this.inside = HTML.createAndAppend("H1", element);
        this.inside.style.paddingLeft = "58px";
        return element;
    }

    updateVisual()
    {
        this.inside.innerText = this.unit[0].textContent;
    }
}
class NameModuleChild extends NameModule
{
    async init(cellId)
    {
        this.cellId = cellId;
        await this.loadUnits();
        let elem = super.createObject();
        this.updateVisual();
        elem.onclick = ()=>{
            let tAr = HTML.create("TEXTAREA");
            tAr.value = this.unit[0].textContent;
            let p = HTML.createPopup(tAr, async ()=>{
                this.unit[0].textContent = tAr.value;
                super.updateVisual();
                await HTML.sendJSON(["pages", "save_unit"], this.unit[0]);
            });
            HTML.addStyles(["fullScreenPopup"],[p]);
        };
        return elem;
    }
    async loadUnits()
    {
        this.unit = await HTML.getJSON(["pages", "get_all_units", this.cellId]);
        if(this.unit.length < 1)
        {
            this.unit.push(await HTML.getEmptyUnit(this.cellId));
        }
    }
}
export{NameModule, NameModuleChild}