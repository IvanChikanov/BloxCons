import { HTMLFather as HTML } from "../system_js/HTMLFather.js";
class InteractiveMap
{
    cellId;
    unit;
    unitsLib = new Map();
    mainFieldGrid;
    intervalId;
    init(cellId)
    {
        this.cellId = cellId;
        return this.createElement();
    }
    createElement()
    {
        this.mainFieldGrid = HTML.create("DIV");
        HTML.addStyles(["grid", "maxWidthInherit", "widthInherit"],[this.mainFieldGrid]);
        this.mainFieldGrid.style.width = "100%";
        return this.mainFieldGrid;
    }
    updateVisual()
    {
        this.textDiv.innerHTML = this.unit[0].textContent;
    }
}
class InteractiveMapChild
{
    preparedField;
    async init(cellId)
    {
        this.cellId = cellId;
        this.unitsLib.set("marker", []);
        await this.loadUnits();
        super.createElement();
        if(this.unitsLib.get("generalMap").image == null)
        {
            this.setImage();
        }
        else
        {         
            this.mainFieldGrid.appendChild(this.createImg(this.unitsLib.get("generalMap").contentTypeImg + "," + this.unitsLib.get("generalMap").image));
            this.intervalId = setInterval(()=>{
                if(this.mainFieldGrid.querySelector("img"))
                {
                    clearInterval(this.intervalId);
                    for(let m of this.unitsLib.get("marker"))
                    {
                        this.markerRender(m, this.mainFieldGrid.querySelector("img").getBoundingClientRect());
                    }
                }
            }, 100);
        }
        return this.mainFieldGrid;
    }
    updateImage(file)
    {
        let img;
        let freader = new FileReader();
        freader.onload = ()=>
        {
            img = this.createImg(freader.result);
            this.unitsLib.get("generalMap").contentTypeImg = freader.result.split(",")[0];
            let ByteReader = new FileReader();
            ByteReader.onload = async ()=>{
                let buffer = new Uint8Array(ByteReader.result);
                let buffer2 = buffer.join(",");
                this.unitsLib.get("generalMap").image = buffer2.split(",").map(Number);
                await HTML.sendJSON(["pages", "save_unit"], this.unitsLib.get("generalMap"));
                this.mainFieldGrid.replaceChild(img, this.mainFieldGrid.querySelector("input"));
                };
            ByteReader.readAsArrayBuffer(file);
        }
        freader.readAsDataURL(file);      
    }
    createImg(src)
    {
        let img = HTML.create("IMG"); 
        img.style.gridArea = "1/1/1/1";
        HTML.addStyles(["maxWidthInherit", "widthInherit"],[img]);
        img.onclick = async (e)=>{
            let unit = await HTML.getEmptyUnit(this.cellId);
            unit.tag = "marker";
            let rect = img.getBoundingClientRect();
            unit.otherJsonOption = JSON.stringify({
                x: e.offsetX / rect.width * 100,
                y: e.offsetY / rect.height * 100
            });
            let mainDiv = HTML.create("DIV");
            HTML.addStyles(["grid"], [mainDiv]);
            let choose = HTML.create("DIV");
            HTML.addStyles(["grid", "gapFivePix", "justifySelfCenter"], [choose]);
            choose.style = "grid-auto-flow: column;";
            let butsAr = [];
            for(let mark of ["dots", "flags", "pips"])
            {
                let but = HTML.createAndAppend("BUTTON", choose);
                let m = HTML.createAndAppend("img", but);
                m.src = `../static/images/markers/${mark}/${document.documentElement.getAttribute("theme")}.svg`;
                m.style.maxWidth = "50px";
                m.style.maxHeight = "50px";
                but.onclick = ()=>{
                    let other = JSON.parse(unit.otherJsonOption);
                    other["src"] = m.src.split(document.documentElement.getAttribute("theme"))[0];
                    unit.otherJsonOption = JSON.stringify(other);
                };
                butsAr.push(but);
                choose.appendChild(but);
            }
            for(let b of butsAr)
            {
                b.addEventListener("click", (e)=>{this.bactiveArray(e, butsAr)});
            }
            mainDiv.appendChild(choose);
            let typeButtonsBlock = HTML.create("DIV");
            let context = HTML.createAndAppend("BUTTON", typeButtonsBlock);
            context.innerText = "context";
            let popupMarker = HTML.createAndAppend("BUTTON", typeButtonsBlock);
            popupMarker.innerText = "popup";
            let relation = HTML.createAndAppend("BUTTON", typeButtonsBlock);
            relation.innerText = "relation";
            HTML.addStyles(["grid", "gapFivePix", "justifySelfCenter"], [typeButtonsBlock]);
            typeButtonsBlock.style = "grid-auto-flow: column;";
            for(let b of [context, popupMarker, relation])
            {
                b.addEventListener("click", (e)=>{this.bactiveArray(e, [context, popupMarker, relation])});
            }
            mainDiv.appendChild(typeButtonsBlock);
            let pops = HTML.createPopup(mainDiv, closeAct.bind(this));
            HTML.addStyles(["fullScreenPopup"], [pops]);
            function closeAct()
            {
                this.markerRender(unit, rect);
                HTML.sendJSON(["pages", "save_unit"], unit);
                this.unitsLib.get("marker").push(unit);
            }
        };
        img.src = src;
        this.mainFieldGrid.appendChild(this.tools());
        return img;
    }
    bactiveArray(e, array)
    {
        for(let button of array)
        {
            if(button == e.target)
            {
                button.classList.add("bactive");
            }
            else
            {
                button.classList.remove("bactive");
            }
        }
    }
    markerRender(markerUnit, rect)
    {
        let marker = HTML.create("img");
        marker.style.position = "absolute";
        if(rect.width <= rect.height)
        {
            marker.style.width = rect.width / 100 * 10 + "px";
        }
        else
        {
            marker.style.height = rect.height / 100 * 10 + "px";
        }
        marker.style.left = rect.width / 100 * JSON.parse(markerUnit.otherJsonOption).x + "px";
        marker.style.top = rect.height / 100 * JSON.parse(markerUnit.otherJsonOption).y + rect.top + window.scrollY + "px";
        marker.style.zIndex = "500";
        marker.src = `${JSON.parse(markerUnit.otherJsonOption).src}${document.documentElement.getAttribute("theme")}.svg`;
        marker.style.transform = "translate(-50%, -50%)";
        this.mainFieldGrid.appendChild(marker);
    }
    async loadUnits()
    {
        this.unit = await HTML.getJSON(["pages", "get_all_units", this.cellId]);
        if(this.unit.length == 0)
        {
            this.unitsLib.set("generalMap", await HTML.getEmptyUnit(this.cellId));
            this.unitsLib.get("generalMap").tag = "generalMap";
            await HTML.sendJSON(["pages", "save_unit"], this.unitsLib.get("generalMap"));
        }
        else
        {
            for(let un of this.unit)
            {
                switch(un.tag)
                {
                    case "generalMap":
                        this.unitsLib.set(un.tag, un);
                        break;
                    case "marker":
                        this.unitsLib.get(un.tag).push(un);
                        break;
                }
            }
        }
    }
    tools()
    {
        let tools = HTML.create("DIV");
        tools.style = "grid-area: 1/1/1/1; align-self: end; justify-self: start;z-index: 150;";
        let changeImg = HTML.createAndAppend("BUTTON", tools);
        changeImg.innerText = "Img";
        changeImg.onclick = this.setImage.bind(this);
        let position = HTML.createAndAppend("BUTTON", tools);
        position.innerText = "pos";
        let setDot = HTML.createAndAppend("BUTTON", tools);
        setDot.innerText = "set";
        return tools;
    }
    setImage()
    {
        this.mainFieldGrid.innerHTML = null;
        this.mainFieldGrid.appendChild(HTML.createFileloaderElement("image/png, image/jpg", this.updateImage.bind(this)));
    }
}
export{InteractiveMap, InteractiveMapChild}