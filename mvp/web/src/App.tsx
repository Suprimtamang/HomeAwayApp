import { useEffect, useMemo, useRef, useState, type FormEvent } from 'react'
import {
  ArrowDownUp,
  ArrowRight,
  Check,
  Compass,
  Heart,
  Home,
  MapPin,
  Plus,
  Search,
  SlidersHorizontal,
  Sparkles,
  Star,
  Utensils,
  X,
} from 'lucide-react'
import {
  Circle,
  MapContainer,
  Marker,
  Popup,
  TileLayer,
  useMap,
} from 'react-leaflet'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import './App.css'

type Category = 'LODGING' | 'EATING' | 'LEISURE'
type CategoryFilter = 'ALL' | Category

type ServiceListing = {
  id: number
  name: string
  category: Category
  neighborhood: string
  description: string
  price: number
  priceUnit: string
  rating: number
  reviewCount: number
  latitude: number
  longitude: number
  imageUrl: string
}

const API_URL = import.meta.env.VITE_API_URL ?? 'http://127.0.0.1:8080'
const STARTING_POINT: [number, number] = [38.657, -9.197]
const CATEGORY_LABELS: Record<CategoryFilter, string> = {
  ALL: 'All places',
  LODGING: 'Stay',
  EATING: 'Eat',
  LEISURE: 'Explore',
}
const CATEGORY_ICONS = {
  ALL: Compass,
  LODGING: Home,
  EATING: Utensils,
  LEISURE: Sparkles,
}
const CATEGORY_IMAGES: Record<Category, string> = {
  LODGING:
    'https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?auto=format&fit=crop&w=960&q=85',
  EATING:
    'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=960&q=85',
  LEISURE:
    'https://images.unsplash.com/photo-1502680390469-be75c86b636f?auto=format&fit=crop&w=960&q=85',
}

function pinIcon(price: number, selected: boolean) {
  return L.divIcon({
    className: 'price-pin-wrap',
    html: `<span class="price-pin${selected ? ' price-pin--selected' : ''}">€${price}</span>`,
    iconSize: [74, 36],
    iconAnchor: [37, 36],
  })
}

function RecenterMap({ listing }: { listing: ServiceListing | null }) {
  const map = useMap()

  useEffect(() => {
    if (listing) {
      map.flyTo([listing.latitude, listing.longitude], 15, { duration: 0.6 })
    }
  }, [listing, map])

  return null
}

function App() {
  const [listings, setListings] = useState<ServiceListing[]>([])
  const [category, setCategory] = useState<CategoryFilter>('ALL')
  const [query, setQuery] = useState('')
  const [maxPrice, setMaxPrice] = useState(600)
  const [favorites, setFavorites] = useState<number[]>(() => {
    try {
      return JSON.parse(localStorage.getItem('homeaway-favorites') ?? '[]') as number[]
    } catch {
      return []
    }
  })
  const [showFavorites, setShowFavorites] = useState(false)
  const [sortByPrice, setSortByPrice] = useState(false)
  const [selectedId, setSelectedId] = useState<number | null>(null)
  const [isFormOpen, setIsFormOpen] = useState(false)
  const [isLoading, setIsLoading] = useState(true)
  const [isSaving, setIsSaving] = useState(false)
  const [error, setError] = useState('')
  const [saveError, setSaveError] = useState('')
  const [reloadKey, setReloadKey] = useState(0)
  const searchInput = useRef<HTMLInputElement>(null)

  useEffect(() => {
    function handleSearchShortcut(event: KeyboardEvent) {
      if ((event.metaKey || event.ctrlKey) && event.key.toLowerCase() === 'k') {
        event.preventDefault()
        searchInput.current?.focus()
      }
    }

    window.addEventListener('keydown', handleSearchShortcut)
    return () => window.removeEventListener('keydown', handleSearchShortcut)
  }, [])

  useEffect(() => {
    const controller = new AbortController()
    const params = new URLSearchParams()
    if (query.trim()) params.set('q', query.trim())
    if (category !== 'ALL') params.set('category', category)
    if (category === 'LODGING') params.set('maxPrice', String(maxPrice))

    fetch(`${API_URL}/api/services?${params}`, { signal: controller.signal })
      .then(async (response) => {
        if (!response.ok) throw new Error('Could not load places right now.')
        return (await response.json()) as ServiceListing[]
      })
      .then((data) => {
        setListings(data)
        setError('')
        setSelectedId((current) =>
          current && data.some((listing) => listing.id === current)
            ? current
            : (data[0]?.id ?? null),
        )
      })
      .catch((cause: unknown) => {
        if (cause instanceof DOMException && cause.name === 'AbortError') return
        setError(
          cause instanceof Error
            ? cause.message
            : 'Could not connect to the HomeAway API.',
        )
      })
      .finally(() => {
        if (!controller.signal.aborted) setIsLoading(false)
      })

    return () => controller.abort()
  }, [category, maxPrice, query, reloadKey])

  useEffect(() => {
    localStorage.setItem('homeaway-favorites', JSON.stringify(favorites))
  }, [favorites])

  const visibleListings = useMemo(() => {
    const result = showFavorites
      ? listings.filter((listing) => favorites.includes(listing.id))
      : [...listings]
    return sortByPrice ? result.sort((a, b) => a.price - b.price) : result
  }, [favorites, listings, showFavorites, sortByPrice])

  const selectedListing =
    visibleListings.find((listing) => listing.id === selectedId) ??
    visibleListings[0] ??
    null

  function toggleFavorite(id: number) {
    setFavorites((current) =>
      current.includes(id)
        ? current.filter((favorite) => favorite !== id)
        : [...current, id],
    )
  }

  async function createListing(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setIsSaving(true)
    setSaveError('')

    const formData = new FormData(event.currentTarget)
    const categoryValue = formData.get('category') as Category
    const payload = {
      name: String(formData.get('name')).trim(),
      category: categoryValue,
      neighborhood: String(formData.get('neighborhood')).trim(),
      description: String(formData.get('description')).trim(),
      price: Number(formData.get('price')),
      priceUnit: String(formData.get('priceUnit')),
      rating: 0,
      reviewCount: 0,
      latitude: Number(formData.get('latitude')),
      longitude: Number(formData.get('longitude')),
      imageUrl: CATEGORY_IMAGES[categoryValue],
    }

    try {
      const response = await fetch(`${API_URL}/api/services`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })
      if (!response.ok) {
        const detail = await response.text()
        throw new Error(detail || 'Could not create this place.')
      }
      const created = (await response.json()) as ServiceListing
      setCategory('ALL')
      setQuery('')
      setShowFavorites(false)
      setListings((current) => [created, ...current])
      setSelectedId(created.id)
      setIsFormOpen(false)
    } catch (cause: unknown) {
      setSaveError(
        cause instanceof Error ? cause.message : 'Could not create this place.',
      )
    } finally {
      setIsSaving(false)
    }
  }

  return (
    <div className="app-shell">
      <header className="topbar">
        <a className="brand" href="/" aria-label="HomeAway home">
          <span className="brand-mark">
            <Home size={18} strokeWidth={2.5} />
          </span>
          <span>homeaway</span>
          <span className="brand-period">.</span>
        </a>
        <nav className="topnav" aria-label="Main navigation">
          <a className="topnav-link topnav-link--active" href="#discover">
            Discover
          </a>
          <a className="topnav-link" href="#places">
            For the community
          </a>
        </nav>
        <div className="topbar-actions">
          <button
            className="host-button"
            type="button"
            aria-label="Share a place"
            onClick={() => {
              setSaveError('')
              setIsFormOpen(true)
            }}
          >
            <Plus size={16} />
            <span>Share a place</span>
          </button>
          <button className="avatar" type="button" aria-label="Your profile">
            S
          </button>
        </div>
      </header>

      <main id="discover" className="page">
        <section className="intro">
          <div className="intro-copy">
            <div className="eyebrow">
              <span className="eyebrow-dot" />
              MADE FOR YOUR NEXT CHAPTER
            </div>
            <h1>
              Find your place
              <br />
              <span>around campus.</span>
            </h1>
            <p>
              Good places, kind people, and little discoveries that make
              somewhere new feel like home.
            </p>
          </div>
          <div className="intro-note">
            <span className="note-icon">
              <Sparkles size={17} />
            </span>
            <span>
              <strong>A little closer to home</strong>
              <small>Student-loved spots near NOVA FCT</small>
            </span>
            <ArrowRight className="note-arrow" size={17} />
          </div>
        </section>

        <section className="discovery" id="places">
          <div className="discovery-toolbar">
            <div className="searchbox">
              <Search size={18} />
              <input
                aria-label="Search places"
                placeholder="Try “coffee”, “beach”, “room…”"
                ref={searchInput}
                value={query}
                onChange={(event) => {
                  setIsLoading(true)
                  setError('')
                  setQuery(event.target.value)
                }}
              />
              <kbd>⌘ K</kbd>
            </div>
            <div className="location-select">
              <MapPin size={16} />
              <span>Costa da Caparica</span>
            </div>
            <button
              className="filter-button"
              type="button"
              onClick={() => {
                setIsLoading(true)
                setError('')
                setCategory('LODGING')
                setShowFavorites(false)
              }}
            >
              <SlidersHorizontal size={16} />
              <span>Rent budget</span>
            </button>
          </div>

          <div className="discovery-heading">
            <div>
              <span className="section-kicker">YOUR NEIGHBORHOOD GUIDE</span>
              <h2>Explore what’s nearby</h2>
            </div>
            <button
              className={`saved-toggle${showFavorites ? ' saved-toggle--active' : ''}`}
              type="button"
              onClick={() => setShowFavorites((current) => !current)}
              aria-pressed={showFavorites}
            >
              <Heart size={16} fill={showFavorites ? 'currentColor' : 'none'} />
              Saved
              {favorites.length > 0 && (
                <span className="saved-count">{favorites.length}</span>
              )}
            </button>
          </div>

          <div className="category-row" role="tablist" aria-label="Place type">
            {(Object.keys(CATEGORY_LABELS) as CategoryFilter[]).map((value) => {
              const Icon = CATEGORY_ICONS[value]
              return (
                <button
                  className={`category-tab${category === value ? ' category-tab--active' : ''}`}
                  key={value}
                  type="button"
                  role="tab"
                  aria-selected={category === value}
                  onClick={() => {
                    setIsLoading(true)
                    setError('')
                    setCategory(value)
                    setShowFavorites(false)
                  }}
                >
                  <Icon size={16} />
                  {CATEGORY_LABELS[value]}
                </button>
              )
            })}
            {category === 'LODGING' && (
              <label className="price-control">
                <span>Monthly budget</span>
                <strong>€{maxPrice}</strong>
                <input
                  aria-label="Maximum monthly rent"
                  type="range"
                  min="250"
                  max="800"
                  step="10"
                  value={maxPrice}
                  onChange={(event) => {
                    setIsLoading(true)
                    setError('')
                    setMaxPrice(Number(event.target.value))
                  }}
                />
              </label>
            )}
            <button
              className="sort-button"
              type="button"
              aria-pressed={sortByPrice}
              onClick={() => setSortByPrice((current) => !current)}
            >
              <ArrowDownUp size={15} />
              {sortByPrice ? 'Price: low to high' : 'Recommended'}
            </button>
          </div>

          <div className="results-meta">
            <span>
              <strong>{visibleListings.length}</strong>{' '}
              {visibleListings.length === 1 ? 'place' : 'places'} to feel at home
            </span>
            <span className="results-area">
              <MapPin size={13} />
              Costa da Caparica, Portugal
            </span>
          </div>

          <div className="results-layout">
            <section className="listing-column" aria-label="Places">
              {error && (
                <div className="state-card state-card--error" role="alert">
                  <span className="state-icon">
                    <Compass size={20} />
                  </span>
                  <div>
                    <strong>We couldn’t reach the place guide</strong>
                    <p>{error} Make sure the Java API is running on port 8080.</p>
                    <button
                      className="retry-button"
                      type="button"
                      onClick={() => {
                        setIsLoading(true)
                        setError('')
                        setReloadKey((current) => current + 1)
                      }}
                    >
                      Try again
                    </button>
                  </div>
                </div>
              )}
              {isLoading && !error && (
                <div className="loading-grid" aria-label="Loading places">
                  {Array.from({ length: 4 }, (_, index) => (
                    <div className="skeleton-card" key={index}>
                      <div className="skeleton-image" />
                      <div className="skeleton-line" />
                      <div className="skeleton-line skeleton-line--short" />
                    </div>
                  ))}
                </div>
              )}
              {!isLoading && !error && visibleListings.length === 0 && (
                <div className="state-card">
                  <span className="state-icon">
                    <Heart size={20} />
                  </span>
                  <div>
                    <strong>
                      {showFavorites ? 'No saved places just yet' : 'No places found'}
                    </strong>
                    <p>
                      {showFavorites
                        ? 'Tap the heart on a place to keep it close.'
                        : 'Try a different search or open up your filters.'}
                    </p>
                  </div>
                </div>
              )}
              {!isLoading && !error && visibleListings.length > 0 && (
                <div className="listing-grid">
                  {visibleListings.map((listing) => (
                    <article
                      className={`listing-card${selectedId === listing.id ? ' listing-card--selected' : ''}`}
                      id={`listing-${listing.id}`}
                      key={listing.id}
                    >
                      <button
                        className="listing-card__open"
                        type="button"
                        onClick={() => setSelectedId(listing.id)}
                      >
                        <span className="listing-image-wrap">
                          <img
                            className="listing-image"
                            src={listing.imageUrl}
                            alt=""
                            loading="lazy"
                          />
                          <span className={`type-label type-label--${listing.category.toLowerCase()}`}>
                            {CATEGORY_LABELS[listing.category]}
                          </span>
                          {listing.category === 'LODGING' && (
                            <span className="image-caption">A good place to land</span>
                          )}
                        </span>
                        <span className="listing-info">
                          <span className="listing-title-row">
                            <strong>{listing.name}</strong>
                              <span className="rating">
                                {listing.reviewCount > 0 ? (
                                  <>
                                    <Star size={13} fill="currentColor" />
                                    {listing.rating.toFixed(1)}
                                  </>
                                ) : (
                                  'New'
                                )}
                              </span>
                          </span>
                          <span className="listing-neighborhood">
                            {listing.neighborhood}
                          </span>
                          <span className="listing-footer">
                            <span className="listing-reviews">
                              {listing.reviewCount > 0
                                ? `${listing.reviewCount} kind words`
                                : 'Just added'}
                            </span>
                            <span className="listing-price">
                              <strong>€{listing.price}</strong> / {listing.priceUnit}
                            </span>
                          </span>
                        </span>
                      </button>
                      <button
                        className={`favorite-button${favorites.includes(listing.id) ? ' favorite-button--active' : ''}`}
                        type="button"
                        aria-label={
                          favorites.includes(listing.id)
                            ? `Remove ${listing.name} from saved places`
                            : `Save ${listing.name}`
                        }
                        onClick={() => toggleFavorite(listing.id)}
                      >
                        <Heart
                          size={18}
                          fill={favorites.includes(listing.id) ? 'currentColor' : 'none'}
                        />
                      </button>
                    </article>
                  ))}
                </div>
              )}
              <div className="community-callout">
                <div className="callout-avatars" aria-hidden="true">
                  <span>J</span>
                  <span>M</span>
                  <span>A</span>
                  <span>+</span>
                </div>
                <p>
                  Made better by the people who live here.
                  <strong> Know a lovely local spot?</strong>
                </p>
                <button
                  type="button"
                  onClick={() => {
                    setSaveError('')
                    setIsFormOpen(true)
                  }}
                >
                  Share it <ArrowRight size={14} />
                </button>
              </div>
            </section>

            <aside className="map-panel" aria-label="Map of nearby places">
              <div className="map-topline">
                <span>
                  <span className="live-dot" />
                  THE NEIGHBORHOOD
                </span>
                <button
                  type="button"
                  aria-label="Reset map view"
                  onClick={() =>
                    setSelectedId(
                      listings.find((listing) => listing.category === 'LODGING')?.id ??
                        null,
                    )
                  }
                >
                  <Compass size={15} />
                </button>
              </div>
              <MapContainer
                center={STARTING_POINT}
                zoom={13}
                scrollWheelZoom
                className="leaflet-map"
                zoomControl={false}
              >
                <TileLayer
                  attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                  url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                {visibleListings.map((listing) => (
                  <Marker
                    key={listing.id}
                    position={[listing.latitude, listing.longitude]}
                    icon={pinIcon(listing.price, selectedId === listing.id)}
                    eventHandlers={{ click: () => setSelectedId(listing.id) }}
                  >
                    <Popup>
                      <strong>{listing.name}</strong>
                      <br />
                      {listing.neighborhood} · €{listing.price}/{listing.priceUnit}
                    </Popup>
                  </Marker>
                ))}
                <Circle
                  center={STARTING_POINT}
                  radius={60}
                  pathOptions={{
                    color: '#315b46',
                    fillColor: '#315b46',
                    fillOpacity: 0.14,
                    weight: 2,
                  }}
                />
                <RecenterMap listing={selectedListing} />
              </MapContainer>
              <div className="map-campus-label">
                <span className="campus-dot" />
                NOVA FCT campus
              </div>
              {selectedListing && (
                <button
                  className="map-selected"
                  type="button"
                  onClick={() => {
                    document
                      .getElementById(`listing-${selectedListing.id}`)
                      ?.scrollIntoView({ behavior: 'smooth', block: 'center' })
                  }}
                >
                  <img src={selectedListing.imageUrl} alt="" />
                  <span>
                    <strong>{selectedListing.name}</strong>
                    <small>{selectedListing.neighborhood}</small>
                  </span>
                  <span className="map-selected-price">€{selectedListing.price}</span>
                </button>
              )}
              <div className="map-attribution-note">
                <MapPin size={13} />
                <span>Good things, just around the corner</span>
              </div>
            </aside>
          </div>
        </section>

        <footer className="footer">
          <a className="brand brand--footer" href="/">
            <span className="brand-mark">
              <Home size={15} strokeWidth={2.5} />
            </span>
            <span>homeaway</span>
            <span className="brand-period">.</span>
          </a>
          <span>Thoughtfully finding your way around Costa da Caparica.</span>
          <span className="footer-credit">A little home, wherever you are <Heart size={13} /></span>
        </footer>
      </main>

      {isFormOpen && (
        <div
          className="modal-backdrop"
          role="presentation"
          onMouseDown={(event) => {
            if (event.target === event.currentTarget) setIsFormOpen(false)
          }}
        >
          <section
            aria-labelledby="share-title"
            aria-modal="true"
            className="share-modal"
            role="dialog"
          >
            <div className="modal-heading">
              <span className="modal-icon">
                <Plus size={20} />
              </span>
              <button
                className="modal-close"
                type="button"
                aria-label="Close form"
                onClick={() => setIsFormOpen(false)}
              >
                <X size={19} />
              </button>
              <span className="section-kicker">A GOOD THING IS BETTER SHARED</span>
              <h2 id="share-title">Add a local place</h2>
              <p>Help someone new find a spot they’ll love.</p>
            </div>
            <form className="share-form" onSubmit={createListing}>
              <label>
                Place name
                <input name="name" required maxLength={100} placeholder="e.g. The sunny little café" />
              </label>
              <div className="form-row">
                <label>
                  Kind of place
                  <select name="category" defaultValue="LODGING">
                    <option value="LODGING">Stay</option>
                    <option value="EATING">Eat</option>
                    <option value="LEISURE">Explore</option>
                  </select>
                </label>
                <label>
                  Neighborhood
                  <input name="neighborhood" required placeholder="Costa da Caparica" />
                </label>
              </div>
              <label>
                A few kind words
                <textarea
                  name="description"
                  required
                  maxLength={1200}
                  rows={3}
                  placeholder="What makes this place special?"
                />
              </label>
              <div className="form-row">
                <label>
                  Price (€)
                  <input name="price" type="number" min="1" required defaultValue="25" />
                </label>
                <label>
                  Price for
                  <select name="priceUnit" defaultValue="month">
                    <option value="month">month</option>
                    <option value="meal">meal</option>
                    <option value="visit">visit</option>
                    <option value="session">session</option>
                    <option value="day">day</option>
                  </select>
                </label>
              </div>
              <div className="form-row">
                <label>
                  Latitude
                  <input name="latitude" type="number" step="any" required defaultValue="38.657" />
                </label>
                <label>
                  Longitude
                  <input name="longitude" type="number" step="any" required defaultValue="-9.197" />
                </label>
              </div>
              {saveError && <p className="form-error" role="alert">{saveError}</p>}
              <button className="submit-button" type="submit" disabled={isSaving}>
                {isSaving ? 'Sharing your place…' : 'Share with the community'}
                {!isSaving && <ArrowRight size={16} />}
              </button>
              <p className="form-footnote">
                <Check size={13} /> Your place will appear on the neighborhood guide.
              </p>
            </form>
          </section>
        </div>
      )}
    </div>
  )
}

export default App
