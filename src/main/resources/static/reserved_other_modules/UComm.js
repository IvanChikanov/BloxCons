class UComm
{
    static async getEmpty(cell_id)
    {
        let empty = await UComm.#send(`/pages/get_empty_unit/${cell_id}`);
        return empty;
    }
    static async save(unit)
    {
        await UComm.#send("/pages/save_unit", {
            method: "POST",
            headers: {"Content-type": "application/json"},
            body: JSON.stringify(unit)
        });
    }
    static async delete(id)
    {
        await UComm.#send("/pages/delete_unit/" + id);
    }
    static async #send(pathValue, post = null)
    {
        let request = await fetch(pathValue, post);
        if(request.headers.get("content-type"))
        {
            let response = await request.json();
            return response;
        }
    }
    static async updateImageUnit(file, unit)
    { 
        let path = "/create_image";
        let formData = new FormData();
        formData.append("image", file);
        if(unit.imageId)
        {
            formData.append("id", unit.imageId);
            path = "/update_image";
        }
        unit.imageId = await UComm.#send(path, {
            method: "POST",
            body: formData
        });
        UComm.save(unit);
    }
    static async getAllUnits(cell_id)
    {
        let all = await UComm.#send(`/pages/get_all_units/${cell_id}`);
        return all;
    }
}
export {UComm};