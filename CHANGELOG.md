# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.0.0] - YYYY-MM-DD
### Added
- Enhanced search endpoint with query parameters for price range.
- Added error handling for not found products.

## [1.1.0] - YYYY-MM-DD
### Added
- `/products/search` endpoint for searching products by keyword.

## [1.0.0] - YYYY-MM-DD
### Added
- Initial release of the product catalogue microservice.
- `/health` endpoint for health checks.
- `/products` endpoint to get all products.
- `/product/{id}` endpoint to get a product by id.
- `/save` endpoint to save a product.
- `/update` endpoint to update a product.
- `/delete/{id}` endpoint to delete a product. 