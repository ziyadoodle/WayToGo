const connection = require('../config/database')

//========================================================================================================

const addPhotoUrlToPlaces = (places) => {
    return places.map(place => {
        const photoUrl = `https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=${place.photo_reference}&key=AIzaSyD-ktXkaMsEdsxDQuMxJS0qit0O6GXvxQU`;
        return { ...place, photoUrl };
    });
};

// Get All Place with Pagination
const place = (req, res) => {
    const page = parseInt(req.query.page) || 1;
    const pageSize = parseInt(req.query.pageSize) || 10;

    const offset = (page - 1) * pageSize;

    connection.query(
        'SELECT * FROM place_destination LIMIT ? OFFSET ?',
        [pageSize, offset],
        function (err, rows) {
            if (err) {
                console.log(err);
                res.status(500).json({massage:"Error get place"});
            } else {
                const placesWithPhotoURLs = addPhotoUrlToPlaces(rows);
                res.status(200).json(placesWithPhotoURLs);
            }
        }
    );
};


//Get Place By Name
const placeByName = (req, res) => {
    const placeName = req.params.name;
    connection.query(
        "SELECT * FROM place_destination WHERE name LIKE ?",
        [`%${placeName}%`],
        function (err, rows) {
            if (err) {
                res.status(500).json({massage:"Error get place"});
            } else {
                const placesWithPhotoURLs = addPhotoUrlToPlaces(rows);
                res.status(200).json(placesWithPhotoURLs);
            }
        }
    )
}

//Get Place By City
const placeByCity = (req, res) => {
    const placeCity = req.params.formatted_address;
    connection.query(
        "SELECT * FROM place_destination WHERE formatted_address LIKE ?",
        [`%${placeCity}%`],
        function (err, rows) {
            if (err) {
                res.status(500).json({massage:"Error get place"});
            } else {
                const placesWithPhotoURLs = addPhotoUrlToPlaces(rows);
                res.status(200).json(placesWithPhotoURLs);
            }
        }
    )
}


//Get Detail Place
const detailPlace = (req, res) => {
    const placeId = req.params.place_id;
    connection.query(
        'SELECT * FROM place_destination WHERE place_id = ?',
        [placeId],
        function (err, rows) {
            if (err) {
                res.status(500).json({massage:"Error get place"});
            } else {
                const placesWithPhotoURLs = addPhotoUrlToPlaces(rows);
                res.status(200).json(placesWithPhotoURLs);
            }
        }
    )
}

module.exports = {place, placeByName, detailPlace, placeByCity}